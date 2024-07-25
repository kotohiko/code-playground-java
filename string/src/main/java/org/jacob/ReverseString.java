package org.jacob;

public class ReverseString {
    public static void main(String[] args) {
        StringBuilder str = new StringBuilder("""
                As I turned up the collar on，
                当我翻起我，
                My favorite winter coat，
                最喜爱的冬衣领子时，
                This wind is blowin' my mind，
                这种想法突如其来，
                I see the kids in the street，
                我看到大街上的孩子们，
                With not enough to eat，
                食不果腹，
                Who am I to be blind，
                我是谁，是瞎子吗？
                Pretending not to see their needs，
                怎能装作没看见他们的渴求。""");
        System.out.println(str.reverse());
    }
}
