package kd.bos.zk;

import com.jacob.sys.snowflake.kingdee.bos.util.SystemProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Jacob Suen
 * @since 14:47 8月 12, 2024
 */
public class ZKFactory {

    private static final String ZKADDR_STR = "zkaddreass";
    private static final String ZKROOTPATH_STR = "zkrootpath";
    private static final String ZKSCHEME_STR = "scheme";
    private static final String ZKUSER_STR = "user";
    private static final String ZKPASS_STR = "password";
    public static final ConcurrentHashMap<String, CuratorFramework> poolMap = new ConcurrentHashMap();
    private static Logger logger = Logger.getLogger(ZKFactory.class);
    private static Map<String, Map<String, String>> parsedUrlMap = new ConcurrentHashMap();
    private static final String ZK_TRACE_NAME = "ZKFactory";

    public ZKFactory() {
    }

    static Map<String, CuratorFramework> getPool() {
        return poolMap;
    }

    static void put(String url, CuratorFramework curatorFramework) {
        poolMap.put(url, curatorFramework);
    }

    private static Map<String, String> parseUrl(String url) {
        Map<String, String> m = (Map) parsedUrlMap.get(url);
        if (m != null) {
            return m;
        } else {
            Map<String, String> m = new ConcurrentHashMap();
            int propIndex = url.indexOf(63);
            String _url = url;
            if (propIndex > 0) {
                String[] sl = url.split("\\?");
                _url = sl[0];
                String strUrlParams = sl[1];
                Properties prop = new Properties();

                try {
                    String[] params = null;
                    if (strUrlParams.contains("&")) {
                        params = strUrlParams.split("&");
                    } else {
                        params = new String[]{strUrlParams};
                    }

                    String[] var8 = params;
                    int var9 = params.length;

                    for (int var10 = 0; var10 < var9; ++var10) {
                        String p = var8[var10];
                        if (p.startsWith("password")) {
                            String password = p.substring("password".length() + 1);
                            prop.put("password", password);
                        } else if (p.contains("=")) {
                            String[] param = p.split("=");
                            if (param.length == 1) {
                                prop.put(param[0], "");
                            } else {
                                String key = param[0];
                                String value = param[1];
                                prop.put(key, value);
                            }
                        }
                    }

                    m.put("scheme", prop.getProperty("scheme", "digest"));
                    m.put("user", prop.getProperty("user"));
                    String pass = prop.getProperty("password");
                    if (pass != null) {
                        pass = Encrypters.decode(pass);
                        m.put("password", pass);
                    }
                } catch (Exception var15) {
                    logger.error("parse zookeeper url exception", var15);
                }
            }

            m.put("zkaddreass", _getZkAddress(_url));
            m.put("zkrootpath", _getZkRootPath(_url));
            parsedUrlMap.put(url, m);
            return m;
        }
    }

    public static CuratorFramework getZKClient(String url) {
        boolean mcIsStarted = Boolean.getBoolean("mc.isstarted");
        TraceSpan span = null;

        CuratorFramework var5;
        try {
            if (mcIsStarted) {
                span = Tracer.create("ZKFactory", "getZKClient", true);
                TopologyTagInject.setCompentTag(span.getInnerSpan(), url, "Zookeeper", "zk");
            }

            Map<String, String> urlInfo = parseUrl(url);
            url = (String) urlInfo.get("zkaddreass");
            if (poolMap.containsKey(url)) {
                CuratorFramework var26 = (CuratorFramework) poolMap.get(url);
                return var26;
            }

            Class var4 = ZKFactory.class;
            synchronized (ZKFactory.class) {
                if (!poolMap.containsKey(url)) {
                    Object retryPolicy;
                    if (Boolean.getBoolean("ha.component.enable") && !StringUtils.isEmpty(SystemProperties.getWithEnv("ha.zookeeper.backup"))) {
                        retryPolicy = new RetryNTimes(Integer.getInteger("zookeeper.client.retry.times", 3), Integer.getInteger("zookeeper.client.retry.sleep.time", 1000));
                    } else {
                        retryPolicy = new RetryForever(Integer.getInteger("zookeeper.client.retry.intervalMs", 10000));
                    }

                    CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder().connectString(url).sessionTimeoutMs(Integer.getInteger("curator-default-session-timeout", 60000)).connectionTimeoutMs(Integer.getInteger("curator-default-connection-timeout", 15000)).retryPolicy((RetryPolicy) retryPolicy);
                    String user = (String) urlInfo.get("user");
                    String pass = (String) urlInfo.get("password");
                    if (user != null && pass != null) {
                        final String auth = user + ":" + pass;
                        ACLProvider aclProvider = new ACLProvider() {
                            private List<ACL> acl;

                            public List<ACL> getDefaultAcl() {
                                if (this.acl == null) {
                                    ArrayList<ACL> acl = Ids.CREATOR_ALL_ACL;
                                    acl.clear();
                                    acl.add(new ACL(31, new Id("auth", auth)));
                                    this.acl = acl;
                                }

                                return this.acl;
                            }

                            public List<ACL> getAclForPath(String path) {
                                return this.acl;
                            }
                        };
                        builder.authorization((String) urlInfo.get("scheme"), auth.getBytes());
                        builder.aclProvider(aclProvider);
                    }

                    CuratorFramework client = new CuratorFrameworkWrapper(builder.build());
                    client.start();
                    poolMap.put(url, client);
                    if (!Env.isMicroKernel()) {
                        KeepAliveService.registerKeepAliveListener(new ZkKeepAliveListener(url, client));
                    }

                    CuratorFrameworkWrapper var29 = client;
                    return var29;
                }

                var5 = (CuratorFramework) poolMap.get(url);
            }
        } catch (Exception var24) {
            logger.error(var24.getMessage(), var24);
            throw new snowflake.kingdee.bos.exception.KDException(var24, BosErrorCode.configZookeepConfig, new Object[]{var24.getMessage()});
        } finally {
            if (span != null) {
                try {
                    span.close();
                } catch (Throwable var22) {
                    logger.error(var22.getMessage(), var22);
                }
            }

        }

        return var5;
    }

    public static String getAuthority(String url) {
        Map<String, String> urlInfo = parseUrl(url);
        String user = (String) urlInfo.get("user");
        String pass = (String) urlInfo.get("password");
        return user != null && pass != null ? user + ":" + pass : null;
    }

    public static String getZkAddress(String url) {
        Map<String, String> urlInfo = parseUrl(url);
        return (String) urlInfo.get("zkaddreass");
    }

    private static String _getZkAddress(String url) {
        int index = url.indexOf(47);
        return index > 0 ? url.substring(0, index) : url;
    }

    public static String getZkRootPath(String url) {
        Map<String, String> urlInfo = parseUrl(url);
        return urlInfo.get("zkrootpath");
    }

    private static String _getZkRootPath(String url) {
        int index = url.indexOf(47);
        if (index > 0) {
            String r = url.substring(index);
            if (!r.endsWith("/")) {
                r = r + "/";
            }

            return r;
        } else {
            return "/";
        }
    }

    static {
        System.setProperty("zookeeper.sasl.client", System.getProperty("zookeeper.sasl.client", "false"));
        System.setProperty("jdk.tls.rejectClientInitiatedRenegotiation", System.getProperty("jdk.tls.rejectClientInitiatedRenegotiation", "true"));
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            poolMap.forEach((k, v) -> {
                try {
                    v.getZookeeperClient().getZooKeeper().close();
                    logger.info("close zookeeperConnection on shutdown");
                } catch (Exception var3) {
                    logger.error("error on ShutdownHook of " + k, var3);
                }

            });
        }));
    }
}