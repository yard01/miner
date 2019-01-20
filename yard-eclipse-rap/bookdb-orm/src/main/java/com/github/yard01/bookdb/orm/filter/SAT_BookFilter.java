package com.github.yard01.bookdb.orm.filter;

import com.github.yard01.bookdb.orm.SAT_Book;

public class SAT_BookFilter extends SAT_Book implements IFilter {

	@Override
	public String getFilterString(String alias) {
		// TODO Auto-generated method stub
		if (alias != "") alias += ".";
		String filter = " and " + alias + "author like '%" + this.getAuthor() + "%'"
				      + " and " + alias + "name like '%"  + this.getName() + "%'"
					  + " and " + alias + "series like '%"  + this.getSeries() + "%'"
					  + " and " + alias + "genre like '%"  + this.getGenre() + "%'"
					  + " and " + alias + "publisher like '%"  + this.getPublisher() + "%'"
					  + " and " + alias + "note like '%"  + this.getNote() + "%'"
				
				      
				      ;
		
		return filter;
	}
	
}
