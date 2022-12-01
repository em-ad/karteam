package space.kheyrati.nanowatch.model;

public class AttendanceState {

    private int color;
    private int percent;

    public AttendanceState() {
    }

    public AttendanceState(int color, int percent) {
        this.color = color;
        this.percent = percent;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }
}
