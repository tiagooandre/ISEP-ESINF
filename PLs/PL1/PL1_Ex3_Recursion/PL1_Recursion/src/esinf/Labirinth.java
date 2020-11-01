
package esinf;

/**
 *
 * @author DEI-ESINF
 */
public class Labirinth {
    
    /**
     *
     * @param actual the labirinth in its actual (marked) form 
     * @param y coordinate y in the labirinth
     * @param x coordinate x in the labirinth
     * @return the marked labirinth or null if there is no way
     */

    public static int [][] check(int [][] actual, int y, int x) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

        actual[y][x] = 9;

        if (y == 6 && x == 12) {
            return actual;
        }

        if (actual[y][x] == 0) {
            return null;
        }

        if (y - 1 >= 0 && actual[y - 1][x] == 1) {
            if (check(actual, y - 1, x) != null) {
                return actual;
            }
        }

         if (x + 1 <= 12 && actual[y][x + 1] == 1) {
             if (check(actual, y, x + 1) != null) {
                 return actual;
             }
        }

        if (y + 1 <= 6 && actual[y + 1][x] == 1) {
            if (check(actual, y + 1, x) != null) {
                return actual;
            }
        }

        if (x - 1 >= 0 && actual[y][x - 1] == 1) {
            if (check(actual, y, x - 1) != null) {
                return actual;
            }
        }

        actual[y][x] = 2;

        return null;
    }

}
