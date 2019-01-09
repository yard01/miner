package tula.yard.games.miner.ui;


import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import tula.yard.games.miner.IGame;

public class InputDialog extends ChildWindow {
  public static final int WINDOW_WIDTH = 250;
  public static final int WINDOW_HEIGHT = 120;
   
  private String message;
  private String input;

  public InputDialog(IGame game, String titleCode, String labelCode) {
    super(game, Display.getCurrent().getActiveShell(), WINDOW_WIDTH, WINDOW_HEIGHT, titleCode);
    setMessage(getLocalizedText(labelCode));
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getInput() {
    return input;
  }

  public void setInput(String input) {
    this.input = input;
  }

  public String open() {
    createContents(this.window);
    this.window.open();
    Display display = this.window.getParent().getDisplay();
    while (!this.window.isDisposed()) {
      if (!display.readAndDispatch()) {
        display.sleep();
      }
    }
    return input;
  }

  private void createContents(final Shell shell) {
    shell.setLayout(new GridLayout(2, true));

    Label label = new Label(shell, SWT.NONE);
    label.setText(message);
    GridData data = new GridData();
    data.horizontalSpan = 2;
    label.setLayoutData(data);

    final Text text = new Text(shell, SWT.BORDER);
    data = new GridData(GridData.FILL_HORIZONTAL);
    data.horizontalSpan = 2;
    text.setLayoutData(data);

    Button ok = new Button(shell, SWT.PUSH);
    ok.setText(getLocalizedText(ChildWindow.OK));
    data = new GridData(GridData.FILL_HORIZONTAL);
    ok.setLayoutData(data);
    ok.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent event) {
        input = text.getText();
        shell.close();
      }
    });

    Button cancel = new Button(shell, SWT.PUSH);
    cancel.setText(getLocalizedText(ChildWindow.CANCEL));
    data = new GridData(GridData.FILL_HORIZONTAL);
    cancel.setLayoutData(data);
    cancel.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent event) {
        input = null;
        shell.close();
      }
    });
    shell.setDefaultButton(ok);
    //shell.pack();
  }
}

