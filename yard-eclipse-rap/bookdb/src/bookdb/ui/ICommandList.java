package bookdb.ui;

import java.util.List;

public interface ICommandList {
	public static final String cmdNew     = "bookdb.cmdNEW"; 
	public static final String cmdEdit    = "bookdb.cmdEDIT"; 
	public static final String cmdDelete  = "bookdb.cmdDELETE"; 
	public static final String cmdView    = "bookdb.cmdVIEW"; 
	public static final String cmdRefresh = "bookdb.cmdREFRESH"; 
	public static final String cmdFilter  = "bookdb.cmdFILTER"; 

	public List<ToolBarCommand> getCommandList();
}
