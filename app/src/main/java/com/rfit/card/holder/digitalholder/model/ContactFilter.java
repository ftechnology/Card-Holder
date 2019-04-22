package com.rfit.card.holder.digitalholder.model;

import android.widget.Filter;

import com.rfit.card.holder.digitalholder.appfrw.DataObject;
import com.rfit.card.holder.digitalholder.appfrw.NotifyObserver;
import com.rfit.card.holder.digitalholder.appfrw.ResponseObject;
import com.rfit.card.holder.digitalholder.entity.ContactEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by farukhossain on 2019/04/18.
 */

public class ContactFilter extends Filter {

    NotifyObserver mObserver = null;
    List<DataObject> mList = null;

    public ContactFilter(List<DataObject> list, NotifyObserver observer) {
        this.mList = list;
        this.mObserver = observer;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();
        // We implement here the filter logic
        if (constraint == null || constraint.length() == 0) {
            // No filter implemented we return all the list
            results.values = mList;
            results.count = mList.size();
        } else {
            // We perform filtering operation
            ArrayList<DataObject> filterList = new ArrayList<DataObject>();

            for (DataObject p : mList) {
                String name = ((ContactEntity) p).getKeyName();

                if (name.toUpperCase().startsWith(constraint.toString().toUpperCase())) {
                    filterList.add(p);
                }

            }

            results.values = filterList;
            results.count = filterList.size();

        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {

        // Now we have to inform the adapter about the new list filtered
        if (mObserver != null) {
            ResponseObject object = new ResponseObject();
            object.setValues(0, "DONE", (ArrayList<DataObject>) results.values);
            mObserver.update(object);
        }

    }

}
