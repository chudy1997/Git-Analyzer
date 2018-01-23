package app.fetch.Test;


import app.fetch.Fetcher;
import app.fetch.GitRevCommits;
import app.fetch.RepoDownloader;
import app.structures.CommitDetails;
import app.structures.FileDiffs;
import com.google.inject.Provider;
import javafx.beans.property.SimpleDoubleProperty;
import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.dircache.DirCache;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Repository;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class TestRepository{

    @Mock
    private Provider<GitRevCommits> gitRevCommitsProvider;

    @Mock
    private Provider<CommitDetails> commitDetailsProvider;

    @Mock
    private Provider<FileDiffs> fileDiffsProvider;

    @Mock
    private RepoDownloader repoDownloader;

    @Mock
    SimpleDoubleProperty progress;

    private Fetcher fetcher;

    private List<Git> gitList = new ArrayList();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(FetcherTest.class);
        fetcher = new Fetcher(repoDownloader, commitDetailsProvider, fileDiffsProvider, gitRevCommitsProvider);
    }

    @Test
    @Ignore
    public void testRealRepo() throws Exception {
        Git git = Git.init().setDirectory(new File("C:\\tmpRepo")).call();
        assertNotNull( git.getRepository().getRef(Constants.HEAD));
        assertTrue(git.status().call().isClean());

        Repository repository = git.getRepository();

        File myFile = new File(repository.getDirectory().getParent(), "testFile");
        if(!myFile.createNewFile()) {
            throw new IOException("Could not create file " + myFile);
        }

        // run the add
        git.add().addFilepattern("testFile").call();

        // and then commit the changes
        git.commit().setMessage("Commit1-Adding testFile").call();

        Path file = new File("C:\\tmpRepo\\testFile2").toPath();
        byte[] buf = "testLine\n testLine2".getBytes();
        Files.write(file, buf);

        git.add().addFilepattern("testFile2").call();
        git.commit().setMessage("Commit2-Adding testFile2").call();

        buf = "testLine\n testLine2\n adddedLine".getBytes();
        Files.write(file, buf);

        git.add().addFilepattern("testFile2").call();
        git.commit().setMessage("Commit3-Changing testFile2").call();

        gitList.add(git);

        Mockito.when(repoDownloader.getRepository("mama", progress)).thenReturn(gitList);
        //Mockito.when(gitRevCommitsProvider.get()).thenReturn()

        fetcher.prepareDownloader("mama", progress);

        fetcher.getAllCommits();




    }



}