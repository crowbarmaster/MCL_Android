package com.example.myapplication.ClassTypes;

import android.util.Log;

import com.example.myapplication.DataManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Note {
    public String ID;
    public Record ParentRecord;
    public String LocalPath;
    public String ServerPath;
    public boolean isLocal;
    public boolean isNew = false;
    public static List<Note> Notes = new ArrayList<>();

    public Note(){}

    public void addNote(Note notes){
        for(Note r: Notes){
            if(r.ID.equals(notes.ID)){
                Log.e("Notes", "Error: note creation attempt failed. An entry with this ID already exists.");
            }else{
                Notes.add(notes);
            }
        }
    }

    public Note(Record record, String path, boolean isServer) {
        if(isServer){

        }else {
            File file = new File(path);
            String ext = path.substring((path.length() - 4));
            this.ParentRecord = record;
            this.LocalPath = path;
            this.isLocal = false;
            switch (ext) {
                case ".txt":
                    this.ServerPath = "MCL_uploads/" + DataManager.user.ID + "_" + DataManager.user.LastName + "/ROOM " + DataManager.record.ID + "/TEXT/" + file.getName();
                    break;
                case "jpeg":
                case ".jpg":
                    this.ServerPath = "MCL_uploads/" + DataManager.user.ID + "_" + DataManager.user.LastName + "/ROOM " + DataManager.record.ID + "/IMAGES/" + file.getName();
                    break;
                case ".3gp":
                    this.ServerPath = "MCL_uploads/" + DataManager.user.ID + "_" + DataManager.user.LastName + "/ROOM " + DataManager.record.ID + "/AUDIO/" + file.getName();
                    break;
            }
            Notes.add(this);
        }
    }

    public String GetFilesString (Record r){
        String tmp = "";
        for(Note note: Notes){
            if(r.ID.equals(note.ParentRecord.ID)){
                tmp = CompileString(tmp, note.ID);
            }
        }
        if(Notes.isEmpty()){
            return "null";
        }
        return "null";
    }

    public String CompileString (String in, String add){
        if(in.isEmpty()){
            return add+";";
        }else {
            return in+add+";";
        }
    }
}
