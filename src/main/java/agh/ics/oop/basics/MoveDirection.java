package agh.ics.oop.basics;

public enum MoveDirection {
    FORWARD,
    UPPPERRIGHT,
    RIGHT,
    LOWERRIGHT,
    BACKWARD,
    LOWERLEFT,
    LEFT,
    UPPERLEFT,
    DIFFERENT;

    public static MoveDirection intToMoveDirection(int that){
        switch (that){
            case 0: return FORWARD;
            case 1: return UPPPERRIGHT;
            case 2: return RIGHT;
            case 3: return LOWERRIGHT;
            case 4: return BACKWARD;
            case 5: return LOWERLEFT;
            case 6: return LEFT;
            case 7: return UPPERLEFT;
            default:
                throw new IllegalStateException("Unexpected value: " + that);
        }
    }

}


