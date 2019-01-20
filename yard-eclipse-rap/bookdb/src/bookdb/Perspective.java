package bookdb;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

/**
 * Configures the perspective layout. This class is contributed through the
 * plugin.xml.
 */
public class Perspective implements IPerspectiveFactory {
	public static final String PERSPECTIVE_PREFIX = "bookdb.ui.perspective.";

	@Override
	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);

		// layout.addStandaloneView(NavigationView.ID, false, IPageLayout.LEFT, 0.25f,
		// editorArea);
		layout.addStandaloneView(LoginViewPart.ID, false, IPageLayout.LEFT, 0.25f, editorArea);

		// IFolderLayout folder = layout.createFolder("booklist", IPageLayout.TOP, 0.5f,
		// editorArea);
		// folder.addPlaceholder(View.ID + ":*");
		// folder.addView(View.ID);

		// layout.getViewLayout(NavigationView.ID).setCloseable(false);
	}
}
