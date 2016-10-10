package jimgv.model;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@RunWith(HierarchicalContextRunner.class)
public class BookTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    private Book book;

    public class ページ変更のイベント通知 {

        private int callCount;

        @Before
        public void setUp() throws Exception {
            folder.newFolder("foo");
            folder.newFile("foo/001.jpg");
            folder.newFile("foo/002.jpg");
            folder.newFile("foo/003.jpg");

            book = new Book(new File(folder.getRoot(), "foo"));
            book.addChangeListener(() -> callCount++);
        }

        @Test
        public void 次のページに遷移したときに通知される() throws Exception {
            // exercise
            book.nextPage();

            // verify
            assertThat(callCount).isEqualTo(1);
        }

        @Test
        public void 前のページに遷移したときに通知される() throws Exception {
            // exercise
            book.nextPage();
            book.previousPage();

            // verify
            assertThat(callCount).isEqualTo(2);
        }

        @Test
        public void Homeに戻ったときに通知される() throws Exception {
            // exercise
            book.nextPage();
            book.home();

            // verify
            assertThat(callCount).isEqualTo(2);
        }

        @Test
        public void Endに戻ったときに通知される() throws Exception {
            // exercise
            book.end();

            // verify
            assertThat(callCount).isEqualTo(1);
        }

        @Test
        public void ページが遷移しない場合は通知されない_Homeと前ページ() throws Exception {
            // exercise
            book.home();
            book.previousPage();

            // verify
            assertThat(callCount).isEqualTo(0);
        }

        @Test
        public void ページが遷移しない場合は通知されない_Endと次ページ() throws Exception {
            // exercise
            book.end(); // 1回目は遷移するが、それ以降は通知されない
            book.end();
            book.nextPage();

            // verify
            assertThat(callCount).isEqualTo(1);
        }
    }

    @Test
    public void ディレクトリは無視される() throws Exception {
        // setup
        folder.newFile("001.jpg");
        folder.newFolder("foo");

        book = new Book(folder.getRoot());

        // exercise, verify
        assertThat(book.getRight()).as("右").hasValue(expectedFile("001.jpg"));
        assertThat(book.getLeft()).as("左").isEmpty();
    }

    @Test
    public void 現在の右ページのインデックス値を取得できる_初期状態() throws Exception {
        // setup
        folder.newFile("001.jpg");
        book = new Book(folder.getRoot());

        // exercise, verify
        assertThat(book.getRightIndex()).isEqualTo(0);
    }

    @Test
    public void 現在の右ページのインデックス値を取得できる_ページ遷移後() throws Exception {
        // setup
        folder.newFile("001.jpg");
        folder.newFile("002.jpg");
        folder.newFile("003.jpg");
        book = new Book(folder.getRoot());
        book.nextPage();

        // exercise, verify
        assertThat(book.getRightIndex()).isEqualTo(2);
    }

    public class ファイルが１つも存在しないディレクトリが設定された場合 {

        @Before
        public void setUp() throws Exception {
            book = new Book(folder.getRoot());
        }

        @Test
        public void 左右のファイルは空を返す() throws Exception {
            // exercise
            Optional<File> right = book.getRight();
            Optional<File> left = book.getLeft();

            // verify
            assertThat(right).as("右ファイル").isEmpty();
            assertThat(left).as("左ファイル").isEmpty();
        }

        @Test
        public void 総ページ数は0() throws Exception {
            // exercise, verify
            assertThat(book.size()).isEqualTo(0);
        }
    }

    public class ファイルが１つだけ存在する場合 {

        @Before
        public void setUp() throws Exception {
            folder.newFile("001.jpg");
            book = new Book(folder.getRoot());
        }

        @Test
        public void 右のファイルは唯一のファイルを返す() throws Exception {
            // exercise
            Optional<File> right = book.getRight();

            // verify
            assertThat(right).hasValue(expectedFile("001.jpg"));
        }

        @Test
        public void 左のファイルは空を返す() throws Exception {
            // exercise
            Optional<File> left = book.getLeft();

            // verify
            assertThat(left).isEmpty();
        }

        @Test
        public void 次のページに遷移しても_左右のページは変化しない() throws Exception {
            // exercise
            book.nextPage();

            // verify
            assertThat(book.getRight()).as("右").hasValue(expectedFile("001.jpg"));
            assertThat(book.getLeft()).as("左").isEmpty();
        }

        @Test
        public void 前のページに遷移しても_左右のページは変化しない() throws Exception {
            // exercise
            book.previousPage();

            // verify
            assertThat(book.getRight()).as("右").hasValue(expectedFile("001.jpg"));
            assertThat(book.getLeft()).as("左").isEmpty();
        }

        @Test
        public void 総ページ数は1() throws Exception {
            // exercise, verify
            assertThat(book.size()).isEqualTo(1);
        }
    }

    public class ファイルが２つだけ存在する {

        @Before
        public void setUp() throws Exception {
            folder.newFile("001.jpg");
            folder.newFile("002.jpg");
            book = new Book(folder.getRoot());
        }

        @Test
        public void 名前順にソートした最初のファイルが右ページとして取得できる() throws Exception {
            // exercise
            Optional<File> right = book.getRight();

            // verify
            assertThat(right).hasValue(expectedFile("001.jpg"));
        }

        @Test
        public void 名前順にソートした２番目のファイルが左ページとして取得できる() throws Exception {
            // exercise
            Optional<File> left = book.getLeft();

            // verify
            assertThat(left).hasValue(expectedFile("002.jpg"));
        }

        @Test
        public void 次のページに遷移しても_左右のページは変化しない() throws Exception {
            // exercise
            book.nextPage();

            // verify
            assertThat(book.getRight()).as("右").hasValue(expectedFile("001.jpg"));
            assertThat(book.getLeft()).as("左").hasValue(expectedFile("002.jpg"));
        }

        @Test
        public void 前のページに遷移しても_左右のページは変化しない() throws Exception {
            // exercise
            book.previousPage();

            // verify
            assertThat(book.getRight()).as("右").hasValue(expectedFile("001.jpg"));
            assertThat(book.getLeft()).as("左").hasValue(expectedFile("002.jpg"));
        }

        @Test
        public void 総ページ数は2() throws Exception {
            // exercise, verify
            assertThat(book.size()).isEqualTo(2);
        }
    }

    public class ファイルが３つだけ存在する {

        @Before
        public void setUp() throws Exception {
            folder.newFile("001.jpg");
            folder.newFile("002.jpg");
            folder.newFile("003.jpg");
            book = new Book(folder.getRoot());
        }

        @Test
        public void 次のページへ遷移すると_３番目のページが右ページになる() throws Exception {
            // exercise
            book.nextPage();

            // verify
            assertThat(book.getRight()).hasValue(expectedFile("003.jpg"));
        }

        @Test
        public void 次のページへ遷移すると_左ページは空() throws Exception {
            // exercise
            book.nextPage();

            // verify
            assertThat(book.getLeft()).isEmpty();
        }
    }

    public class ファイルが４つだけ存在する {

        @Before
        public void setUp() throws Exception {
            folder.newFile("001.jpg");
            folder.newFile("002.jpg");
            folder.newFile("003.jpg");
            folder.newFile("004.jpg");
            book = new Book(folder.getRoot());
        }

        @Test
        public void 次のページへ遷移すると_３番目のページが右ページになる() throws Exception {
            // exercise
            book.nextPage();

            // verify
            assertThat(book.getRight()).hasValue(expectedFile("003.jpg"));
        }

        @Test
        public void 次のページへ遷移すると_４番目のページが左ページになる() throws Exception {
            // exercise
            book.nextPage();

            // verify
            assertThat(book.getLeft()).hasValue(expectedFile("004.jpg"));
        }

        @Test
        public void 一度次のページに進んでから_再びページを戻ると_最初の状態に戻る() throws Exception {
            // exercise
            book.nextPage();
            book.previousPage();

            // verify
            assertThat(book.getRight()).as("右").hasValue(expectedFile("001.jpg"));
            assertThat(book.getLeft()).as("左").hasValue(expectedFile("002.jpg"));
        }
    }

    public class 開始ページに左を設定した場合 {
        @Before
        public void setUp() throws Exception {
            folder.newFile("001.jpg");
            folder.newFile("002.jpg");
            folder.newFile("003.jpg");
            book = new Book(folder.getRoot());
            book.setStartWithLeft(true);
        }

        @Test
        public void 初期状態では右ページが空になる() throws Exception {
            // exercise, verify
            assertThat(book.getRight()).isEmpty();
        }

        @Test
        public void 初期状態では左ページが１つ目のファイルになる() throws Exception {
            // exercise, verify
            assertThat(book.getLeft()).hasValue(expectedFile("001.jpg"));
        }
    }

    @Test
    public void 開始ページを左にしてから次ページに移動して前ページに戻ったときのインデックス値チェック() throws Exception {
        // setup
        folder.newFile("001.jpg");
        folder.newFile("002.jpg");
        book = new Book(folder.getRoot());

        // exercise
        book.setStartWithLeft(true);
        book.nextPage();
        book.previousPage();

        // verify
        assertThat(book.getRight()).as("右").isEmpty();
        assertThat(book.getLeft()).as("左").hasValue(expectedFile("001.jpg"));
    }

    private File expectedFile(String name) {
        return new File(this.folder.getRoot(), name);
    }
}