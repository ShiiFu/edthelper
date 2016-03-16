package com.application.android.edthelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EvenementBdd {
    private static final int version = 1;
    private static final String BDD = "event.bd";

    private SQLiteDatabase bdd;

    private EventSql eventSql;

    public EvenementBdd(Context context) {
        eventSql = new EventSql(context, BDD, null, version);
    }

    public void eraseBdd() {
        this.eventSql.onUpgrade(bdd, 1, 1);
    }

    public void openForWrite() {
        bdd = eventSql.getWritableDatabase();
    }

    public void openForRead() {
        bdd = eventSql.getReadableDatabase();
    }

    public void close() {
        bdd.close();
    }

    public SQLiteDatabase getBdd() {
        return bdd;
    }

    public long insertEvent(Evenement evenement) {
        ContentValues content = new ContentValues();
        content.put(EventSql.DESCRIPTION, evenement.getDescription());
        content.put(EventSql.LIEU, evenement.getLieu());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        content.put(EventSql.DEBUT, dateFormat.format(evenement.getDebut()));
        content.put(EventSql.FIN, dateFormat.format(evenement.getFin()));
        return bdd.insert(EventSql.TABLE, null, content);
    }

    public int updateEvent(Evenement evenement, int id) {
        ContentValues content = new ContentValues();
        content.put(EventSql.DESCRIPTION, evenement.getDescription());
        content.put(EventSql.LIEU, evenement.getLieu());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        content.put(EventSql.DEBUT, dateFormat.format(evenement.getDebut()));
        content.put(EventSql.FIN, dateFormat.format(evenement.getFin()));
        return bdd.update(EventSql.TABLE, content, EventSql.ID + " = " + id, null);
    }

    public int removeEvent(int id) {
        return bdd.delete(EventSql.TABLE, EventSql.ID + " = " + id, null);
    }

    public Evenement getEvenement(int id) {
        Cursor c = bdd.query(EventSql.TABLE, new String[] {EventSql.ID, EventSql.DESCRIPTION, EventSql.LIEU, EventSql.DEBUT, EventSql.FIN}, EventSql.ID + " LIKE \"" + id + "\"", null, null, null, EventSql.DEBUT);
        return cursorToChapter(c);
    }

    public Evenement cursorToChapter(Cursor c) {
        if (c.getCount() == 0) {
            c.close();
            return null;
        }
        Evenement evenement = new Evenement();
        evenement.setId(c.getInt(EventSql.NUMID));
        evenement.setDescription(c.getString(EventSql.NUMDESCRIPTION));
        evenement.setLieu(c.getString(EventSql.NUMLIEU));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            evenement.setDebut(dateFormat.parse(c.getString(EventSql.NUMDEBUT)));
            evenement.setFin(dateFormat.parse(c.getString(EventSql.NUMFIN)));
        } catch (ParseException e) {
            Log.e("Parser date", "e: " + e.toString());
        }
        c.close();
        return evenement;
    }

    public ArrayList<Evenement> getAllEvent() {
        Cursor c = bdd.query(EventSql.TABLE, new String[] {EventSql.ID, EventSql.DESCRIPTION, EventSql.LIEU, EventSql.DEBUT, EventSql.FIN}, null, null, null, null, EventSql.DEBUT);
        if (c.getCount() == 0) {
            c.close();
            return null;
        }
        ArrayList<Evenement> eventList = new ArrayList<Evenement>();
        while (c.moveToNext()) {
            Evenement evenement = new Evenement();
            evenement.setId(c.getInt(EventSql.NUMID));
            evenement.setDescription(c.getString(EventSql.NUMDESCRIPTION));
            evenement.setLieu(c.getString(EventSql.NUMLIEU));
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                evenement.setDebut(dateFormat.parse(c.getString(EventSql.NUMDEBUT)));
                evenement.setFin(dateFormat.parse(c.getString(EventSql.NUMFIN)));
            } catch (ParseException e) {
                Log.e("Parser date", "e: " + e.toString());
            }
            eventList.add(evenement);
        }
        c.close();
        return eventList;
    }

    public ArrayList<String> getAllEventString() {
        Cursor c = bdd.query(EventSql.TABLE, new String[] {EventSql.ID, EventSql.DESCRIPTION, EventSql.LIEU, EventSql.DEBUT, EventSql.FIN}, null, null, null, null, EventSql.DEBUT);
        if (c.getCount() == 0) {
            c.close();
            return null;
        }
        ArrayList<String> eventList = new ArrayList<String>();
        while (c.moveToNext()) {
            String res = c.getString(EventSql.NUMDESCRIPTION)
                    + c.getString(EventSql.NUMDEBUT) + "\n"
                    + c.getString(EventSql.NUMFIN) + "\n\n";
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                if (dateFormat.parse(c.getString(EventSql.NUMFIN)).compareTo(new Date()) > 0)
                    eventList.add(res);
            } catch (ParseException e) {
                Log.e("Parser date", "e: " + e.toString());
            }
        }
        c.close();
        return eventList;
    }
}
