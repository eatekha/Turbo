import java.util.Arrays;

public class TestClass {
    private void horizantalRead(String line, int current){
        for (int i = 1; i < line.length(); i+=2) {
            if (line.charAt(i)!= 'B') {
                int x = current + 1;
                System.out.println(current + "--link--" + x);
            }
            current++;
        }
    }
    private void verticalRead(String line, int current, int width){
        for (int i = 0; i < line.length(); i+=2) {
            if (line.charAt(i) != 'B') {
                int x = current + width;
                System.out.println(current + "--link--" + x);
            }
            current++;
        }
    }




    public static void main(String[] args) throws MapException {
        String line = "+P+B+C+";
        String line2 = "+L+L+L+";
        MyMap map = new MyMap("map0");
        TestClass test = new TestClass();
        int current =0, width = 4, first = 0;
        for (int i = 0; i < 2; i++) {
            //if horizantal row
            if (i%2 == 0){
                current = first;
                test.horizantalRead(line,current);
                first +=width;
            }
            else{
                test.verticalRead(line,current,width);
            }


        }
    }
}