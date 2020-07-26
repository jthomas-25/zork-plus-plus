public class Blueprint {

    public void ghostType(String str) {

        char[] chars = str.toCharArray();

        for (char c : chars) {
            System.out.print(c);
        }
        System.out.print('\n');
    }

    public void win() {

        ghostType("But I don't feel right...");

    }
}
