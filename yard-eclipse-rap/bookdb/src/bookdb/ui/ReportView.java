package bookdb.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import bookdb.widget.BookDBWidget;

public class ReportView extends ViewPart {
	public static String ID = "bookdb.ui.viewREPORT";
	protected Browser browser;
	
	public ReportView() {
		
	}

	@Override
	public void createPartControl(Composite parent) {
		// TODO Auto-generated method stub
		parent.setLayout(new FillLayout());
		browser = new Browser(parent, SWT.NONE);
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	public void setBook(BookDBWidget bookdb) {
		// TODO Auto-generated method stub
		this.setPartName(bookdb.getName()); //.setTitleToolTip("WWWWWW");
		String html = "<html>\n\r"
				+"<b>" + bookdb.getAuthor() + " " + '"' + bookdb.getName() + '"' + "</b><br>"
				+"<i>Серия:</i> "+ bookdb.getSeries() + "<br>"
				+"<i>Жанр:</i> "+ bookdb.getGenre() + "<br>" 
				+"<i>Издательство:</i> "+ bookdb.getPublisher() + "<br>" 
				+"<i>Примечание: "+ bookdb.getNote() + "</i><br>" 
					
				+"</html>";
		browser.setText(html);
	}

}
