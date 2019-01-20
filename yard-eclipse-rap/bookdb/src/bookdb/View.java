package bookdb;

import java.util.Dictionary;
import java.util.Hashtable;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.internal.graphics.Graphics;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.ViewPart;

import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;

import bookdb.ui.ICommandList;
import bookdb.ui.ToolBarCommand;
import bookdb.widget.BookDBWidget;
import bookdb.widget.BookDBWidgetServices;

import org.eclipse.nebula.widgets.pagination.collections.PageResultContentProvider;
import org.eclipse.nebula.widgets.pagination.renderers.navigation.ResultAndNavigationPageGraphicsRenderer;
import org.eclipse.nebula.widgets.pagination.renderers.navigation.ResultAndNavigationPageGraphicsRendererFactory;
import org.eclipse.nebula.widgets.pagination.renderers.navigation.graphics.BlackNavigationPageGraphicsConfigurator;
import org.eclipse.nebula.widgets.pagination.renderers.navigation.graphics.BlueNavigationPageGraphicsConfigurator;
import org.eclipse.nebula.widgets.pagination.renderers.navigation.graphics.GreenNavigationPageGraphicsConfigurator;
import org.eclipse.nebula.widgets.pagination.table.PageableTable;
import org.eclipse.nebula.widgets.pagination.table.SortTableColumnSelectionListener;

/**
 * This view shows a &quot;mail message&quot;. This class is contributed through
 * the plugin.xml.
 */
public class View extends ViewPart implements EventHandler {

	public static final String ID = "bookdb.view";
	public static ClassLoader classLoader = View.class.getClassLoader();

	private PageableTable pageableTable;

	protected Dictionary<String, Object> getHandlerServiceProperties(String... topics) {
		Dictionary<String, Object> result = new Hashtable<String, Object>();
		result.put(EventConstants.EVENT_TOPIC, topics);
		return result;
	}

	protected Dictionary<String, Object> getHandlerServiceProperties(String[] topics, String filter) {
		Dictionary<String, Object> result = new Hashtable<String, Object>();
		result.put(EventConstants.EVENT_TOPIC, topics);
		result.put(EventConstants.EVENT_FILTER, filter);
		return result;
	}

	@Override
	public void createPartControl(Composite parent) {
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell().setData(this.getClass().getCanonicalName(),
				this);

		// BundleContext context = InternalPlatform.getDefault().getBundleContext();

		// context.registerService(EventHandler.class.getName(), this,
		// getHandlerServiceProperties(this.getClass().getCanonicalName()));

		FormToolkit toolkit = new FormToolkit(parent.getDisplay());

		Section section = toolkit.createSection(parent, ExpandableComposite.TITLE_BAR);
		section.setLayout(new GridLayout());
		Composite tableComposite = new Composite(section, SWT.NONE);
		tableComposite.setLayout(new GridLayout());
		section.setClient(tableComposite);

		int pageSize = 4;
		pageableTable = new PageableTable(tableComposite, SWT.BORDER,
				SWT.BORDER | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL, pageSize, PageResultContentProvider.getInstance(),
				ResultAndNavigationPageGraphicsRendererFactory.getBlueFactory(), null);

		pageableTable.setLayoutData(new GridData(GridData.FILL_BOTH));

		// 2) Initialize the table viewer + SWT Table
		TableViewer viewer = pageableTable.getViewer();
		viewer.setContentProvider(ArrayContentProvider.getInstance());
		viewer.setLabelProvider(new LabelProvider());

		Table table = viewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		// 3) Create Table columns with sort of paginated list.
		createColumns(viewer);

		// 3) Set current page to 0 to refresh the table
		pageableTable.setPageLoader(new BookDBWidgetServices());
		pageableTable.setCurrentPage(0);

		if (pageableTable.getWidget().getItemCount() > 0)
			pageableTable.getWidget().setSelection(0);

		buildToolBar(section, pageableTable);

	}

	private static void createColumns(final TableViewer viewer) {

		TableViewerColumn col = createTableViewerColumn(viewer, "Author", 150);
		try {
			col.getColumn().setData("get", BookDBWidget.class.getMethod("getAuthor"));
			col.getColumn().setData("set", BookDBWidget.class.getMethod("setAuthor", String.class));
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				BookDBWidget w = (BookDBWidget) element;
				return w.getAuthor();
			}
		});

		col.getColumn().addSelectionListener(new SortTableColumnSelectionListener("author"));

		col = createTableViewerColumn(viewer, "Name", 250);
		try {
			col.getColumn().setData("get", BookDBWidget.class.getMethod("getName"));
			col.getColumn().setData("set", BookDBWidget.class.getMethod("setName", String.class));
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				BookDBWidget w = (BookDBWidget) element;
				String s = w.getName();
				return s;

			}
		});

		col.getColumn().addSelectionListener(new SortTableColumnSelectionListener("name"));

		col = createTableViewerColumn(viewer, "Series", 150);
		try {
			col.getColumn().setData("get", BookDBWidget.class.getMethod("getSeries"));
			col.getColumn().setData("set", BookDBWidget.class.getMethod("setSeries", String.class));
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				BookDBWidget w = (BookDBWidget) element;
				String s = w.getSeries();
				return s;

			}
		});

		col = createTableViewerColumn(viewer, "Genre", 150);
		try {
			col.getColumn().setData("get", BookDBWidget.class.getMethod("getGenre"));
			col.getColumn().setData("set", BookDBWidget.class.getMethod("setGenre", String.class));
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				BookDBWidget w = (BookDBWidget) element;
				String s = w.getGenre();
				return s;

			}
		});

		col = createTableViewerColumn(viewer, "Publisher", 150);
		try {
			col.getColumn().setData("get", BookDBWidget.class.getMethod("getPublisher"));
			col.getColumn().setData("set", BookDBWidget.class.getMethod("setPublisher", String.class));
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				BookDBWidget w = (BookDBWidget) element;
				String s = w.getPublisher();
				return s;

			}
		});

		col = createTableViewerColumn(viewer, "Note", 300);
		try {
			col.getColumn().setData("get", BookDBWidget.class.getMethod("getNote"));
			col.getColumn().setData("set", BookDBWidget.class.getMethod("setNote", String.class));
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				BookDBWidget w = (BookDBWidget) element;
				String s = w.getNote();
				return s;

			}
		});

		/*
		 * 
		 * col = createTableViewerColumn(viewer, "Series", 100);
		 * col.setLabelProvider(new ColumnLabelProvider() {
		 * 
		 * 
		 * col = createTableViewerColumn(viewer, "Genre", 100); col.setLabelProvider(new
		 * ColumnLabelProvider() {
		 * 
		 * @Override public String getText(Object element) { NebulaWidget w =
		 * (NebulaWidget) element; Person p = w.getCommitter(); return p.getFirstName()
		 * + " " + p.getLastName();
		 * 
		 * } }); /* col = createTableViewerColumn(viewer, "Publisher", 100);
		 * col.setLabelProvider(new ColumnLabelProvider() {
		 *
		 * @Override public String getText(Object element) { NebulaWidget w =
		 * (NebulaWidget) element; Person p = w.getCommitter(); return p.getFirstName()
		 * + " " + p.getLastName();
		 *
		 * } }); col = createTableViewerColumn(viewer, "Year", 100);
		 * col.setLabelProvider(new ColumnLabelProvider() {
		 *
		 * @Override public String getText(Object element) { NebulaWidget w =
		 * (NebulaWidget) element; Person p = w.getCommitter(); return p.getFirstName()
		 * + " " + p.getLastName();
		 *
		 * } }); col = createTableViewerColumn(viewer, "Note", 255);
		 * col.setLabelProvider(new ColumnLabelProvider() {
		 *
		 * @Override public String getText(Object element) { NebulaWidget w =
		 * (NebulaWidget) element; Person p = w.getCommitter(); return p.getFirstName()
		 * + " " + p.getLastName();
		 *
		 * } });
		 */

	}

	private static TableViewerColumn createTableViewerColumn(TableViewer viewer, String title, int bound) {
		final TableViewerColumn viewerColumn = new TableViewerColumn(viewer, SWT.NONE);
		final TableColumn column = viewerColumn.getColumn();
		column.setText(title);
		column.setWidth(bound);
		column.setResizable(true);
		column.setMoveable(true);
		return viewerColumn;
	}

	public void createParameters(Composite parent) {
		parent.setLayout(new GridLayout(2, true));
		getSettings(parent);
	}

	private void getSettings(Composite parent) {
		// Style combo
		Label label = new Label(parent, SWT.NONE);
		label.setText("Style:");
		final Combo styleCombo = new Combo(parent, SWT.READ_ONLY);
		styleCombo.setItems(new String[] { "Blue", "Green", "Black" });
		styleCombo.select(0);
		styleCombo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (styleCombo.getText().equals("Blue")) {
					((ResultAndNavigationPageGraphicsRenderer) pageableTable.getCompositeTop()).getNavigationPage()
							.setConfigurator(BlueNavigationPageGraphicsConfigurator.getInstance());
				} else if (styleCombo.getText().equals("Green")) {
					((ResultAndNavigationPageGraphicsRenderer) pageableTable.getCompositeTop())
							.setConfigurator(GreenNavigationPageGraphicsConfigurator.getInstance());
				} else {
					((ResultAndNavigationPageGraphicsRenderer) pageableTable.getCompositeTop())
							.setConfigurator(BlackNavigationPageGraphicsConfigurator.getInstance());
				}

			}
		});
		styleCombo.setLayoutData(new GridData(GridData.FILL_BOTH));

	}

	public String[] createLinks() {
		String[] links = {
				"<a href=\"http://angelozerr.wordpress.com/2012/01/06/nebula_pagination/\" >Pagination Control Article</a>" };
		return links;
	}

	@Override
	public void setFocus() {
	}

	public void refresh() {
		pageableTable.setCurrentPage(0);
		pageableTable.setPageLoader(new BookDBWidgetServices());
		pageableTable.update();
		pageableTable.refreshPage(true);
		pageableTable.redraw();
		if (pageableTable.getWidget().getItemCount() > 0)
			pageableTable.getWidget().setSelection(0);
	}

	public void buildToolBar(Section parent, PageableTable table) {

		Composite toolComposite = new Composite(parent, SWT.NONE);
		GridLayout gl1 = new GridLayout();
		gl1.numColumns = 1;
		toolComposite.setLayout(gl1);

		ToolBar toolBar = new ToolBar(toolComposite, SWT.FLAT);
		parent.setTextClient(toolComposite);

		ICommandList cl = (ICommandList) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell()
				.getData(ICommandList.class.getCanonicalName());
		for (ToolBarCommand cmd : cl.getCommandList()) {
			cmd.setAssignedTable(table);
			ToolItem toolItem = new ToolItem(toolBar, SWT.PUSH | SWT.FLAT);
			toolItem.setData(cmd);

			toolItem.setImage(Graphics.getImage(cmd.getIcon(), classLoader));

			toolItem.setToolTipText(cmd.getName());
			toolItem.addSelectionListener(new SelectionListener() {
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
				}

				@Override
				public void widgetSelected(SelectionEvent e) {
					// System.out.println((e.width.getClass().getName()));
					((ToolBarCommand) e.widget.getData()).execute();

				}
			});
		}

	}

	@Override
	public void handleEvent(Event event) {
		// TODO Auto-generated method stub
		// System.out.println(MessageFormat.format(
		// "Event Handler {0} handled event on topic {1}: Value of 'property1' = {2}",
		// "handler", "ttt", // event.getProperty(EventConstants.EVENT_TOPIC),
		// event.getProperty("property1")));
	}

}
