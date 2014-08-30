package com.nadeem.todos.utils;

import java.util.List;

import com.nadeem.todos.R;
import com.nadeem.todos.R.id;
import com.nadeem.todos.R.layout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


public class ListAdapter extends ArrayAdapter<Todo> {
    private final Context context;
    private final List<Todo> todoList;
	public ListAdapter(Context context, List<Todo> todoList) {
		super(context, R.layout.activity_main, todoList);
		this.context = context;
		this.todoList = todoList;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		 
        View rowView = inflater.inflate(R.layout.activity_main, parent, false);
         
        TextView todoText = (TextView) rowView.findViewById(R.id.todoText);
        TextView time = (TextView) rowView.findViewById(R.id.time);
        todoText.setText(todoList.get(position).getText());
        time.setText(todoList.get(position).getTime());
        return rowView;
	}
	
}
