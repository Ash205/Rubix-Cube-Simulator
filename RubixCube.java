import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class RubixCube {
    public static final String[] COLORS = {"WHITE", "GREEN", "YELLOW", "BLUE", "RED", "ORANGE"};
    public static final char[] MOVE_SET = {'R', 'L', 'U', 'D', 'F', 'B', 'M', 'E', 'S', 'X', 'Y', 'Z', 'r', 'l', 'u', 'd', 'f', 'b', 'm', 'e', 's', 'x', 'y', 'z'};
    public static final char[] MOVE_SUBSET = {'R', 'L', 'U', 'D', 'F', 'B', 'M', 'r', 'l', 'u', 'd', 'f', 'b', 'm'};

    private static HashMap<String, Integer> colorToIndex = new HashMap<>();
    private static final String[] FACES = {"FRONT", "RIGHT", "BACK", "LEFT", "TOP", "BOTTOM"};
    private HashMap<String, Integer> faces = new HashMap<>();
    private int[][][] cube = new int[6][3][3];

    RubixCube() {
        faces.put("FRONT", 0);
        faces.put("RIGHT", 1);
        faces.put("BACK", 2);
        faces.put("LEFT", 3);
        faces.put("TOP", 4);
        faces.put("BOTTOM", 5);

        for (int i=0; i<COLORS.length; i++) {
            colorToIndex.put(COLORS[i], i);
        }

        for (int i=0; i<6; i++) {
            for (int j=0; j<3; j++) {
                for (int k=0; k<3; k++) {
                    cube[i][j][k] = i;
                }
            }
        }
    }

    public void reset() {
        for (int i=0; i<FACES.length; i++) {
            faces.put(FACES[i], i); 
        }

        for (int i=0; i<6; i++) {
            for (int j=0; j<3; j++) {
                for (int k=0; k<3; k++) {
                    cube[i][j][k] = i;
                }
            }
        }
    }

    public void scramble() {
        Random rand = new Random();
        
        System.out.print("Moves : ");
        for (int i=0; i<25; i++) {
            int index = rand.nextInt(MOVE_SUBSET.length);
            char curMove = MOVE_SUBSET[index];
            applyMove(curMove);
            if (Character.isUpperCase(curMove)) {
                System.out.print(curMove + " ");
            } else {
                System.out.print(Character.toUpperCase(curMove) + "' ");
            }
        }
        System.out.println();
        display();
    }

    public boolean isSolved() {
        for (int i=0; i<6; i++) {
            int compare = cube[i][1][1];

            for (int j=0; j<3; j++) {
                for (int k=0; k<3; k++) {
                    if (cube[i][j][k] != compare) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public void display() {
        int f;

        //Display top face
        for (int i=0; i<3; i++) {
            for (int j=0; j<3; j++) {
                System.out.print(" ");
            }
            System.out.print(" ");
            f = faces.get("TOP");
            for (int j=0; j<3; j++) {
                System.out.print(COLORS[cube[f][i][j]].charAt(0));
            }
            System.out.println();
        }

        // Display front four faces
        for (int i=0; i<3; i++) {
            f = faces.get("LEFT");
            for (int j=0; j<3; j++) {
                System.out.print(COLORS[cube[f][i][j]].charAt(0));
            }
            System.out.print(" ");
            f = faces.get("FRONT");
            for (int j=0; j<3; j++) {
                System.out.print(COLORS[cube[f][i][j]].charAt(0));
            }
            System.out.print(" ");
            f = faces.get("RIGHT");
            for (int j=0; j<3; j++) {
                System.out.print(COLORS[cube[f][i][j]].charAt(0));
            }
            System.out.print(" ");
            f = faces.get("BACK");
            for (int j=0; j<3; j++) {
                System.out.print(COLORS[cube[f][i][j]].charAt(0));
            }
            System.out.println();
        }

        //Display bottom face
        for (int i=0; i<3; i++) {
            for (int j=0; j<3; j++) {
                System.out.print(" ");
            }
            System.out.print(" ");
            f = faces.get("BOTTOM");
            for (int j=0; j<3; j++) {
                System.out.print(COLORS[cube[f][i][j]].charAt(0));
            }
            System.out.println();
        }
    }

    public void faceRotate(String targetFace, int turn) {
        int f = faces.get(targetFace);
        int[][] newFace = {
            {0, 0, 0},
            {0, 0, 0},
            {0, 0, 0}
        };
        
        if (turn > 0) {
            for (int t=0; t<turn; t++) {
                for (int i=0; i<3; i++) {
                    for (int j=0; j<3; j++) {
                        newFace[i][j] = cube[f][2-j][i];
                    }
                }
                for (int i=0; i<3; i++) {
                    for (int j=0; j<3; j++) {
                        cube[f][i][j] = newFace[i][j];
                    }
                }
            }
        } else {
            for (int i=0; i<3; i++) {
                for (int j=0; j<3; j++) {
                    newFace[2-j][i] = cube[f][i][j];
                }
            }
            for (int i=0; i<3; i++) {
                for (int j=0; j<3; j++) {
                    cube[f][i][j] = newFace[i][j];
                }
            }
        }       
        
    }

    public void applyMove(String moves) {
        for (int i=0; i<moves.length(); i++) {
            applyMove(moves.charAt(i));
        }
    }

    public void applyMove(char move) {
        int t = faces.get("TOP"),
            f = faces.get("FRONT"),
            b = faces.get("BACK"),
            bo = faces.get("BOTTOM"),
            r = faces.get("RIGHT"),
            l = faces.get("LEFT");
        int[] temp = {0, 0, 0};

        switch(move) {
            case 'R' :
                faceRotate("RIGHT", 1);
                temp[0] = cube[t][0][2]; temp[1] = cube[t][1][2]; temp[2] = cube[t][2][2];
                for (int i=0; i<3; i++) {
                    cube[t][i][2] = cube[f][i][2];
                }
                for (int i=0; i<3; i++) {
                    cube[f][i][2] = cube[bo][i][2];
                }
                for (int i=0; i<3; i++) {
                    cube[bo][i][2] = cube[b][i][0];
                }
                for (int i=0; i<3; i++) {
                    cube[b][i][0] = temp[2-i];
                }
                break;

            case 'r' :
                faceRotate("RIGHT", -1);
                temp[0] = cube[t][0][2]; temp[1] = cube[t][1][2]; temp[2] = cube[t][2][2];
                for (int i=0; i<3; i++) {
                    cube[t][i][2] = cube[b][2-i][0];
                }
                for (int i=0; i<3; i++) {
                    cube[b][i][0] = cube[bo][i][2];
                }
                for (int i=0; i<3; i++) {
                    cube[bo][i][2] = cube[f][i][2];
                }
                for (int i=0; i<3; i++) {
                    cube[f][i][2] = temp[i];
                }
                break;

            case 'L' :
                faceRotate("LEFT", 1);
                temp[0] = cube[t][0][0]; temp[1] = cube[t][1][0]; temp[2] = cube[t][2][0];
                for (int i=0; i<3; i++) {
                    cube[t][i][0] = cube[b][2-i][2];
                }
                for (int i=0; i<3; i++) {
                    cube[b][2-i][2] = cube[bo][i][0];
                }
                for (int i=0; i<3; i++) {
                    cube[bo][i][0] = cube[f][i][0];
                }
                for (int i=0; i<3; i++) {
                    cube[f][i][0] = temp[i];
                }
                break;

            case 'l' :
                faceRotate("LEFT", -1);
                temp[0] = cube[t][0][0]; temp[1] = cube[t][1][0]; temp[2] = cube[t][2][0];
                for (int i=0; i<3; i++) {
                    cube[t][i][0] = cube[f][i][0];
                }
                for (int i=0; i<3; i++) {
                    cube[f][i][0] = cube[bo][i][0];
                }
                for (int i=0; i<3; i++) {
                    cube[bo][i][0] = cube[b][2-i][2];
                }
                for (int i=0; i<3; i++) {
                    cube[b][2-i][2] = temp[i];
                }
                break;
            
            case 'U' :
                faceRotate("TOP", 1);
                temp[0] = cube[f][0][0]; temp[1] = cube[f][0][1]; temp[2] = cube[f][0][2];
                for (int i=0; i<3; i++) {
                    cube[f][0][i] = cube[r][0][i];
                }
                for (int i=0; i<3; i++) {
                    cube[r][0][i] = cube[b][0][i];
                }
                for (int i=0; i<3; i++) {
                    cube[b][0][i] = cube[l][0][i];
                }
                for (int i=0; i<3; i++) {
                    cube[l][0][i] = temp[i];
                }
                break;

            case 'u' :
                faceRotate("TOP", -1);
                temp[0] = cube[f][0][0]; temp[1] = cube[f][0][1]; temp[2] = cube[f][0][2];
                for (int i=0; i<3; i++) {
                    cube[f][0][i] = cube[l][0][i];
                }
                for (int i=0; i<3; i++) {
                    cube[l][0][i] = cube[b][0][i];
                }
                for (int i=0; i<3; i++) {
                    cube[b][0][i] = cube[r][0][i];
                }
                for (int i=0; i<3; i++) {
                    cube[r][0][i] = temp[i];
                }
                break;

            case 'D' :
                faceRotate("BOTTOM", 1);
                temp[0] = cube[f][2][0]; temp[1] = cube[f][2][1]; temp[2] = cube[f][2][2];
                for (int i=0; i<3; i++) {
                    cube[f][2][i] = cube[l][2][i];
                }
                for (int i=0; i<3; i++) {
                    cube[l][2][i] = cube[b][2][i];
                }
                for (int i=0; i<3; i++) {
                    cube[b][2][i] = cube[r][2][i];
                }
                for (int i=0; i<3; i++) {
                    cube[r][2][i] = temp[i];
                }
                break;

            case 'd' :
                faceRotate("BOTTOM", -1);
                temp[0] = cube[f][2][0]; temp[1] = cube[f][2][1]; temp[2] = cube[f][2][2];
                for (int i=0; i<3; i++) {
                    cube[f][2][i] = cube[r][2][i];
                }
                for (int i=0; i<3; i++) {
                    cube[r][2][i] = cube[b][2][i];
                }
                for (int i=0; i<3; i++) {
                    cube[b][2][i] = cube[l][2][i];
                }
                for (int i=0; i<3; i++) {
                    cube[l][2][i] = temp[i];
                }
                break;

            case 'F' :
                faceRotate("FRONT", 1);
                temp[0] = cube[t][2][0]; temp[1] = cube[t][2][1]; temp[2] = cube[t][2][2];
                for (int i=0; i<3; i++) {
                    cube[t][2][i] = cube[l][i][2];
                }
                for (int i=0; i<3; i++) {
                    cube[l][i][2] = cube[bo][0][i];
                }
                for (int i=0; i<3; i++) {
                    cube[bo][0][i] = cube[r][i][0];
                }
                for (int i=0; i<3; i++) {
                    cube[r][i][0] = temp[i];
                }
                break;
            
            case 'f' :
                faceRotate("FRONT", -1);
                temp[0] = cube[t][2][0]; temp[1] = cube[t][2][1]; temp[2] = cube[t][2][2];
                for (int i=0; i<3; i++) {
                    cube[t][2][i] = cube[r][i][0];
                }
                for (int i=0; i<3; i++) {
                    cube[r][i][0] = cube[bo][0][i];
                }
                for (int i=0; i<3; i++) {
                    cube[bo][0][i] = cube[l][i][2];
                }
                for (int i=0; i<3; i++) {
                    cube[l][i][2] = temp[i];
                }
                break;
            
            case 'B' :
                faceRotate("BACK", 1);
                temp[0] = cube[t][0][0]; temp[1] = cube[t][0][1]; temp[2] = cube[t][0][2];
                for (int i=0; i<3; i++) {
                    cube[t][0][i] = cube[r][i][2];
                }
                for (int i=0; i<3; i++) {
                    cube[r][i][2] = cube[bo][2][2-i];
                }
                for (int i=0; i<3; i++) {
                    cube[bo][2][2-i] = cube[l][i][0];
                }
                for (int i=0; i<3; i++) {
                    cube[l][i][0] = temp[i];
                }
                break;
            
            case 'b' :
                faceRotate("BACK", -1);
                temp[0] = cube[t][0][0]; temp[1] = cube[t][0][1]; temp[2] = cube[t][0][2];
                for (int i=0; i<3; i++) {
                    cube[t][0][i] = cube[l][i][0];
                }
                for (int i=0; i<3; i++) {
                    cube[l][i][0] = cube[bo][2][2-i];
                }
                for (int i=0; i<3; i++) {
                    cube[bo][2][2-i] = cube[r][i][2];
                }
                for (int i=0; i<3; i++) {
                    cube[r][i][2] = temp[i];
                }
                break;

            case 'M' :
                temp[0] = cube[t][0][1]; temp[1] = cube[t][1][1]; temp[2] = cube[t][2][1];
                for (int i=0; i<3; i++) {
                    cube[t][i][1] = cube[b][2-i][1];
                }
                for (int i=0; i<3; i++) {
                    cube[b][2-i][1] = cube[bo][i][1];
                }
                for (int i=0; i<3; i++) {
                    cube[bo][i][1] = cube[f][i][1];
                }
                for (int i=0; i<3; i++) {
                    cube[f][i][1] = temp[i];
                }
                break;

            case 'm' :
                temp[0] = cube[t][0][1]; temp[1] = cube[t][1][1]; temp[2] = cube[t][2][1];
                for (int i=0; i<3; i++) {
                    cube[t][i][1] = cube[f][i][1];
                }
                for (int i=0; i<3; i++) {
                    cube[f][i][1] = cube[bo][i][1];
                }
                for (int i=0; i<3; i++) {
                    cube[bo][i][1] = cube[b][2-i][1];
                }
                for (int i=0; i<3; i++) {
                    cube[b][2-i][1] = temp[i];
                }
                break;

            case 'E' :
                temp[0] = cube[f][1][0]; temp[1] = cube[f][1][1]; temp[2] = cube[f][1][2];
                for (int i=0; i<3; i++) {
                    cube[f][1][i] = cube[l][1][i];
                }
                for (int i=0; i<3; i++) {
                    cube[l][1][i] = cube[b][1][i];
                }
                for (int i=0; i<3; i++) {
                    cube[b][1][i] = cube[r][1][i];
                }
                for (int i=0; i<3; i++) {
                    cube[r][1][i] = temp[i];
                }
                break;

            case 'e' :
                temp[0] = cube[f][1][0]; temp[1] = cube[f][1][1]; temp[2] = cube[f][1][2];
                for (int i=0; i<3; i++) {
                    cube[f][1][i] = cube[r][1][i];
                }
                for (int i=0; i<3; i++) {
                    cube[r][1][i] = cube[b][1][i];
                }
                for (int i=0; i<3; i++) {
                    cube[b][1][i] = cube[l][1][i];
                }
                for (int i=0; i<3; i++) {
                    cube[l][1][i] = temp[i];
                }
                break;

            case 'S' :
                temp[0] = cube[t][1][0]; temp[1] = cube[t][1][1]; temp[2] = cube[t][1][2];
                for (int i=0; i<3; i++) {
                    cube[t][1][2-i] = cube[l][i][1];
                }
                for (int i=0; i<3; i++) {
                    cube[l][i][1] = cube[bo][1][i];
                }
                for (int i=0; i<3; i++) {
                    cube[bo][1][i] = cube[r][2-i][1];
                }
                for (int i=0; i<3; i++) {
                    cube[r][i][1] = temp[2-i];
                }
                break;
            
            case 's' :
                temp[0] = cube[t][1][0]; temp[1] = cube[t][1][1]; temp[2] = cube[t][1][2];
                for (int i=0; i<3; i++) {
                    cube[t][1][i] = cube[r][i][1];
                }
                for (int i=0; i<3; i++) {
                    cube[r][i][1] = cube[bo][1][2-i];
                }
                for (int i=0; i<3; i++) {
                    cube[bo][1][i] = cube[l][i][1];
                }
                for (int i=0; i<3; i++) {
                    cube[l][i][1] = temp[2-i];
                }
                break;
            
            case 'X' :
                applyMove("Rml");
                break;
            
            case 'Y' :
                applyMove("Ued");
                break;

            case 'Z' :
                applyMove("FSb");
                break;
                
            case 'x' :
                applyMove("rML");
                break;

            case 'y' :
                applyMove("uED");
                break;

            case 'z' :
                applyMove("fsB");
                break;

            default :
                System.out.println("Invalid Move!");
        }

    }

    public void applyMoveInSteps(String moves) {
        for (int i=0; i<moves.length(); i++) {
            applyMove(moves.charAt(i));
            System.out.println(moves.charAt(i));
            display();
            System.out.println();
        }
    }

    public void menu() {
        boolean stop = false;
        int option;
        Scanner scan = new Scanner(System.in);

        while (!stop) {
            System.out.println("\n---Rubix Cube Simulator---");
            System.out.println("1. Reset");
            System.out.println("2. Scramble");
            System.out.println("3. Apply Move(s)");
            System.out.println("4. Display");
            System.out.println("5. Quit");
            System.out.print("\n Enter any option : ");
            option = scan.nextInt();

            switch(option) {
                case 1:
                    reset();
                    display();
                    System.out.println("->Cube Reseted");
                    break;
                case 2:
                    scramble();
                    System.out.println("->Cube Scrambled");
                    break;
                case 3:
                    System.out.print("Enter one or more moves : ");
                    String m = scan.next();
                    applyMove(m);
                    display();
                    System.out.println("->Moves Applied");
                    break;
                case 4:
                    display();
                    break;
                case 5:
                    stop = true;
                    break;
                default: 
                System.out.println("Invalid Option");
            }
        }
        scan.close();
    }

    public static void main(String[] args) {
        RubixCube c = new RubixCube();
        c.menu();
    }
}
