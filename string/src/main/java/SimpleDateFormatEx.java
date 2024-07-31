import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Jacob Suen
 * @apiNote 2024 年就职于金蝶期间，某天编码遇到了一个不可思议的业务场景卡壳。拿到一个{@link java.util.Date}对象，如何转为国内流行的
 * {@code 20**-**-**}格式？试了好多种方案都没达到满意效果，后来问了 AI 模型才拿到了达到预期效果的解决方案。那便是通过
 * {@link java.text.SimpleDateFormat}来达成需求效果。
 * @since 14:02 Jul 30, 2024
 */
public class SimpleDateFormatEx {
    public static void main(String[] args) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        System.out.println("Construct a Date object directly is like: [" + date + "]");
        // SimpleDateFormat.format方法原生地支持解析Date对象，所以就格外方便
        System.out.println("After formatted by SimpleDateFormat is like: [" + sdf.format(date) + "]");
    }
}