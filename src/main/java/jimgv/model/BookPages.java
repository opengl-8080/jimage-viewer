package jimgv.model;

public class BookPages {
    private static final int NO_CONTENTS_INDEX = -1;

    private final int maxSize;
    private int currentPageNumber = 1;
    private boolean startWithLeft;

    public BookPages(int maxSize) {
        this.maxSize = maxSize;
    }

    BookPages() {
        this(Integer.MAX_VALUE - 1);
    }

    public int getRightIndex() {
        if (this.maxSize == 0) {
            return NO_CONTENTS_INDEX;
        }

        if (this.startWithLeft) {
            return (this.currentPageNumber * 2) - 3;
        } else {
            return (this.currentPageNumber - 1) * 2;
        }
    }

    public int getLeftIndex() {
        int leftIndex = this.getRightIndex() + 1;
        return (this.getMaxIndex() < leftIndex) ? NO_CONTENTS_INDEX : leftIndex;
    }

    public void nextPage() {
        if (this.currentPageNumber + 1 <= this.getMaxPageNumber()) {
            this.currentPageNumber += 1;
        }
    }

    public void previousPage() {
        if (1 <= this.currentPageNumber - 1) {
            this.currentPageNumber -= 1;
        }
    }

    private int getMaxIndex() {
        return this.maxSize - 1;
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

    int getMaxPageNumber() {
        if (this.maxSize == 0) {
            return 0;
        }

        int maxSize = this.startWithLeft ? this.maxSize + 1 : this.maxSize;

        return (maxSize / 2) + (maxSize % 2);
    }

    public void home() {
        this.currentPageNumber = 1;
    }

    public void end() {
        this.currentPageNumber = this.getMaxPageNumber();
    }

    int getCurrentPageNumber() {
        return this.currentPageNumber;
    }

    public void setCurrentPageNumber(int currentPageNumber) {
        if (this.getMaxPageNumber() < currentPageNumber) {
            this.currentPageNumber = this.getMaxPageNumber();
        } else if (currentPageNumber < 1) {
            this.currentPageNumber = 1;
        } else {
            this.currentPageNumber = currentPageNumber;
        }
    }
}
