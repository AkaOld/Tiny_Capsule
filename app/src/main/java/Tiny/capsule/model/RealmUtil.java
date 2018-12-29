package Tiny.capsule.model;

import android.content.Context;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RealmResults;

public class RealmUtil {
    static private Realm realm = null;
    static private RealmUtil instance = new RealmUtil();
    private RealmUtil(){}
    public static void init(Context context){
        Realm.init(context);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);
    }

    static public RealmUtil getInstance() {
        return instance;
    }

    public void saveInDatabase(RealmObject object){
        realm.beginTransaction();
        realm.copyToRealm(object);
        realm.commitTransaction();
    }

    public void saveInDatabase(ArrayList objects){
        realm.beginTransaction();
        for(Object object:objects){
            RealmObject realmObject = (RealmObject) object;
            realm.copyToRealm(realmObject);
        }
        realm.commitTransaction();
    }

    public void deleteFromDatabase(RealmResults objects){
        realm.beginTransaction();
        objects.deleteAllFromRealm();
        realm.commitTransaction();
    }


    static private Realm realm(){
        return realm;
    }
    static public Realm getRealm(){
        return realm();
    }
}
