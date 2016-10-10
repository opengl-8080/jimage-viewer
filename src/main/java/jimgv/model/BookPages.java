package jimgv.model;

public class BookPages {

    private int pageNumber;
    private boolean startWithLeft;

    public int getRightIndex() {
        return this.getPageNumber();
    }

    public int getLeftIndex() {
        return this.getPageNumber() + 1;
    }

    public void nextPage() {
        this.pageNumber += 2;
    }

    public void previousPage() {
        this.pageNumber -= 2;
    }

    private int getPageNumber() {
        return this.startWithLeft ? this.pageNumber - 1 : this.pageNumber;
    }

    void setStartWithLeft(boolean startWithLeft) {
        this.startWithLeft = startWithLeft;
    }

    public boolean isStartWithLeft() {
        return this.startWithLeft;
    }

    public void switchStartWithLeft() {
        this.startWithLeft = !this.startWithLeft;
    }
}
