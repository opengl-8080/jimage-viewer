package jimgv.model;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.*;

@RunWith(HierarchicalContextRunner.class)
public class BookPagesTest {

    private BookPages pages = new BookPages();

    public class デフォルト {

        @Test
        public void 生成直後は右ページが0で左ページが1() throws Exception {
            // exercise, verify
            assertThat(pages.getRightIndex()).as("right").isEqualTo(0);
            assertThat(pages.getLeftIndex()).as("left").isEqualTo(1);
        }

        @Test
        public void 次のページへ進めたら左右それぞれのページが２加算される() throws Exception {
            // exercise
            pages.nextPage();

            // verify
            assertThat(pages.getRightIndex()).as("right").isEqualTo(2);
            assertThat(pages.getLeftIndex()).as("left").isEqualTo(3);
        }

        @Test
        public void 前のページに戻ったら左右それぞれのページが２減算される() throws Exception {
            // setup
            pages.nextPage(); // 2, 3
            pages.nextPage(); // 4, 5
            pages.nextPage(); // 6, 7

            // exercise
            pages.previousPage();

            // verify
            assertThat(pages.getRightIndex()).as("right").isEqualTo(4);
            assertThat(pages.getLeftIndex()).as("left").isEqualTo(5);
        }

        @Test
        public void 左ページ開始はfalse() throws Exception {
            // exercise, verify
            assertThat(pages.isStartWithLeft()).isFalse();
        }

        @Test
        public void 右ページが存在しない状態で前ページに遷移しても状態は変化しない() throws Exception {
            // exercise
            pages.previousPage();

            // verify
            assertThat(pages.getRightIndex()).as("right").isEqualTo(0);
            assertThat(pages.getLeftIndex()).as("left").isEqualTo(1);
        }

        @Test
        public void 左ページが存在しない場合はマイナス1がインデックス値となる() throws Exception {
            // setup
            pages = new BookPages(1);

            // verify
            assertThat(pages.getRightIndex()).as("right").isEqualTo(0);
            assertThat(pages.getLeftIndex()).as("left").isEqualTo(-1);
        }

        @Test
        public void 左ページが存在しない状態で次ページに遷移しても状態は変化しない() throws Exception {
            // setup
            pages = new BookPages(4);

            // exercise
            pages.nextPage();

            // verify
            assertThat(pages.getRightIndex()).as("before right").isEqualTo(2);
            assertThat(pages.getLeftIndex()).as("before left").isEqualTo(3);

            // exercise
            pages.nextPage();

            // verify
            assertThat(pages.getRightIndex()).as("after right").isEqualTo(2);
            assertThat(pages.getLeftIndex()).as("after left").isEqualTo(3);
        }
    }

    public class 左ページ開始の場合 {
        @Before
        public void setUp() throws Exception {
            pages.setStartWithLeft(true);
        }

        @Test
        public void 生成直後は右ページがマイナス1で左ページが0() throws Exception {
            // exercise, verify
            assertThat(pages.getRightIndex()).as("right").isEqualTo(-1);
            assertThat(pages.getLeftIndex()).as("left").isEqualTo(0);
        }

        @Test
        public void 次のページへ進めたら左右それぞれのページが２加算される() throws Exception {
            // exercise
            pages.nextPage();

            // verify
            assertThat(pages.getRightIndex()).as("right").isEqualTo(1);
            assertThat(pages.getLeftIndex()).as("left").isEqualTo(2);
        }

        @Test
        public void 前のページに戻ったら左右それぞれのページが２減算される() throws Exception {
            // setup
            pages.nextPage(); // 1, 2
            pages.nextPage(); // 3, 4
            pages.nextPage(); // 5, 6

            // exercise
            pages.previousPage();

            // verify
            assertThat(pages.getRightIndex()).as("right").isEqualTo(3);
            assertThat(pages.getLeftIndex()).as("left").isEqualTo(4);
        }

        @Test
        public void 左ページ開始はtrue() throws Exception {
            // exercise, verify
            assertThat(pages.isStartWithLeft()).isTrue();
        }

        @Test
        public void 右ページが存在しない状態で前ページに遷移しても状態は変化しない() throws Exception {
            // exercise
            pages.previousPage();

            // verify
            assertThat(pages.getRightIndex()).as("right").isEqualTo(-1);
            assertThat(pages.getLeftIndex()).as("left").isEqualTo(0);
        }

        @Test
        public void 左ページが存在しない場合はマイナス1がインデックス値となる() throws Exception {
            // setup
            pages = new BookPages(2);
            pages.setStartWithLeft(true);
            pages.nextPage();

            // verify
            assertThat(pages.getRightIndex()).as("right").isEqualTo(1);
            assertThat(pages.getLeftIndex()).as("left").isEqualTo(-1);
        }

        @Test
        public void 左ページが存在しない状態で次ページに遷移しても状態は変化しない() throws Exception {
            // setup
            pages = new BookPages(4);
            pages.setStartWithLeft(true);

            // exercise
            pages.nextPage();
            pages.nextPage();

            // verify
            assertThat(pages.getRightIndex()).as("before right").isEqualTo(3);
            assertThat(pages.getLeftIndex()).as("before left").isEqualTo(-1);

            // exercise
            pages.nextPage();

            // verify
            assertThat(pages.getRightIndex()).as("after right").isEqualTo(3);
            assertThat(pages.getLeftIndex()).as("after left").isEqualTo(-1);
        }
    }

    public class 最大サイズに0を設定した場合 {
        @Before
        public void setup() throws Exception {
            pages = new BookPages(0);
        }

        @Test
        public void ページインデックスは左右共にマイナス1() throws Exception {
            // verify
            assertThat(pages.getRightIndex()).as("right").isEqualTo(-1);
            assertThat(pages.getLeftIndex()).as("left").isEqualTo(-1);
        }
    }

    public class 最大ページ数の取得 {

        public class 右ページ始まり {

            @Test
            public void 最大サイズが0の場合最大ページは0() throws Exception {
                // setup
                pages = new BookPages(0);

                // exercise, verify
                assertThat(pages.getMaxPageNumber()).isEqualTo(0);
            }

            @Test
            public void 最大サイズが1の場合最大ページは1() throws Exception {
                // setup
                pages = new BookPages(1);

                // exercise, verify
                assertThat(pages.getMaxPageNumber()).isEqualTo(1);
            }

            @Test
            public void 最大サイズが2の場合最大ページは1() throws Exception {
                // setup
                pages = new BookPages(2);

                // exercise, verify
                assertThat(pages.getMaxPageNumber()).isEqualTo(1);
            }

            @Test
            public void 最大サイズが3の場合最大ページは2() throws Exception {
                // setup
                pages = new BookPages(3);

                // exercise, verify
                assertThat(pages.getMaxPageNumber()).isEqualTo(2);
            }
        }

        public class 左ページ始まり {

            @Test
            public void 最大サイズが0の場合最大ページは0() throws Exception {
                // setup
                pages = new BookPages(0);
                pages.setStartWithLeft(true);

                // exercise, verify
                assertThat(pages.getMaxPageNumber()).isEqualTo(0);
            }

            @Test
            public void 最大サイズが1の場合最大ページは1() throws Exception {
                // setup
                pages = new BookPages(1);
                pages.setStartWithLeft(true);

                // exercise, verify
                assertThat(pages.getMaxPageNumber()).isEqualTo(1);
            }

            @Test
            public void 最大サイズが2の場合最大ページは2() throws Exception {
                // setup
                pages = new BookPages(2);
                pages.setStartWithLeft(true);

                // exercise, verify
                assertThat(pages.getMaxPageNumber()).isEqualTo(2);
            }

            @Test
            public void 最大サイズが3の場合最大ページは2() throws Exception {
                // setup
                pages = new BookPages(3);
                pages.setStartWithLeft(true);

                // exercise, verify
                assertThat(pages.getMaxPageNumber()).isEqualTo(2);
            }
        }
    }

    @Test
    public void 左ページ開始をスイッチできる() throws Exception {
        assertThat(pages.isStartWithLeft()).as("1").isFalse();

        pages.switchStartWithLeft();

        assertThat(pages.isStartWithLeft()).as("2").isTrue();

        pages.switchStartWithLeft();

        assertThat(pages.isStartWithLeft()).as("3").isFalse();
    }
}