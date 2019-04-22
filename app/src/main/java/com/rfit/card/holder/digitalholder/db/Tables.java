/**
 * @author Faruk Hossain
 * The table information like list of create table and drop tables
 */
package com.rfit.card.holder.digitalholder.db;

import com.rfit.card.holder.digitalholder.entity.ContactEntity;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class Tables extends ArrayList<String> {

    // The list of drop tables
    protected ArrayList<String> mListDropTables = new ArrayList<String>();

    /**
     * Constructor function.
     */
    public Tables() {
        createTableScript();
        //dropTableScript();
    }

    /**
     * Return the drop table list
     *
     * @return
     */
    public ArrayList<String> getDropTableList() {

        return this.mListDropTables;
    }

    /**
     * Add all the create table script here.
     */
    public void createTableScript() {
        // TODO add more
        this.add(ContactEntity.getCreateTable());

        // Add more tables here....
    }

    /**
     * Add all the drop table script here.
     */
    public void dropTableScript() {
        // TODO add more
        mListDropTables.add(ContactEntity.getDropTable());
        // Add more tables here....
    }
}
