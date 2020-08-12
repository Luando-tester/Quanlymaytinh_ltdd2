package com.example.quanlysuachuamaytinh;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;


public class DataHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "quan_li_phieu_thu";
    private static final int DB_VERSION = 1;

    private static final String TABLE_NAME = "phieuthu";

    private static final String COULUMN_ID = "_id";
    private static final String COULUMN_MA_PHIEU_THU = "ma_phieuthu";
    private static final String COULUMN_MA_KHACH_HANG = "ma_khach_hang";
    private static final String COULUMN_NGAY = "ngay";
    private static final String COULUMN_LOAI_PHIEU = "loai_phieu";
    private static final String COULUMN_SO_TIEN = "so_tien";

    public DataHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
                "CREATE TABLE " +
                        TABLE_NAME + " (" +
                        COULUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COULUMN_MA_PHIEU_THU + " TEXT" +
                        COULUMN_MA_KHACH_HANG +"TEXT"+
                        COULUMN_NGAY + "TEXT" +
                        COULUMN_LOAI_PHIEU + "TEXT" +
                        COULUMN_SO_TIEN + "INTEGER" +
                      ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void updatePT(PhieuThu oldPhieuThu, PhieuThu newPhieuThu){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COULUMN_MA_PHIEU_THU,newPhieuThu.getSoPT());
        values.put(COULUMN_MA_KHACH_HANG,newPhieuThu.getMaKh());
        values.put(COULUMN_NGAY,newPhieuThu.getNgay());
        values.put(COULUMN_LOAI_PHIEU,newPhieuThu.getLoaphieu());
        values.put(COULUMN_SO_TIEN,newPhieuThu.getSotien());

        db.update(TABLE_NAME,
                values,
                COULUMN_MA_PHIEU_THU + " =? AND " + COULUMN_MA_KHACH_HANG + " =? AND " + COULUMN_NGAY + " =? AND " + COULUMN_LOAI_PHIEU + " =? AND " + COULUMN_SO_TIEN + " =? ",
                new String[] {oldPhieuThu.getSoPT(),oldPhieuThu.getMaKh(), oldPhieuThu.getNgay(),oldPhieuThu.getLoaphieu(), oldPhieuThu.getSotien() + ""});
        db.close();
    }

    public void onWriteDatabase(ArrayList<PhieuThu> phieuthus){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        for (PhieuThu phieuthu : phieuthus) {
            values.put(COULUMN_MA_PHIEU_THU,phieuthu.getSoPT());
            values.put(COULUMN_MA_KHACH_HANG,phieuthu.getMaKh());
            values.put(COULUMN_NGAY,phieuthu.getNgay());
            values.put(COULUMN_LOAI_PHIEU,phieuthu.getLoaphieu());
            values.put(COULUMN_SO_TIEN,phieuthu.getSotien());
            db.insert(TABLE_NAME,null,values);
        }
        db.close();
    }

    public void onReadDatabase(ArrayList<PhieuThu> phieuthus){
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(TABLE_NAME,new String[] {COULUMN_MA_PHIEU_THU, COULUMN_MA_KHACH_HANG, COULUMN_NGAY,COULUMN_LOAI_PHIEU,COULUMN_SO_TIEN},null,null,null,null,null);
        if (cursor.moveToFirst()) {
            do {
                String maPT = cursor.getString(cursor.getColumnIndex(COULUMN_MA_PHIEU_THU));
                String maKH = cursor.getString(cursor.getColumnIndex(COULUMN_MA_KHACH_HANG));
                String ngay = cursor.getString(cursor.getColumnIndex(COULUMN_NGAY));
                String loaiphieu = cursor.getString(cursor.getColumnIndex(COULUMN_LOAI_PHIEU));
                int sotien = Integer.parseInt(cursor.getString(cursor.getColumnIndex(COULUMN_SO_TIEN)));
                phieuthus.add(new PhieuThu(maPT,maKH,ngay,loaiphieu,sotien));
            }while (cursor.moveToNext());
        }
        db.close();
    }

    public void deleteDatbase(PhieuThu phieuthus){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME,
                COULUMN_MA_PHIEU_THU + " =? AND " + COULUMN_MA_KHACH_HANG + " =? AND " + COULUMN_NGAY + " =? AND " + COULUMN_LOAI_PHIEU + " =? AND "  + COULUMN_SO_TIEN + " =? ",
                new String[] {phieuthus.getSoPT(),phieuthus.getMaKh(), phieuthus.getNgay(),phieuthus.getLoaphieu(), phieuthus.getSotien() + ""});
        db.close();
    }

    public boolean findMember(PhieuThu phieuthus){
        boolean flag = false;
        SQLiteDatabase db = getWritableDatabase();

        Cursor cursor = db.query(TABLE_NAME,
                new String[] {COULUMN_MA_PHIEU_THU,COULUMN_MA_KHACH_HANG,COULUMN_NGAY,COULUMN_SO_TIEN},
                COULUMN_MA_PHIEU_THU + " =? AND " + COULUMN_MA_KHACH_HANG + " =? AND " + COULUMN_NGAY + " =? AND "+ COULUMN_LOAI_PHIEU + " =? AND "  + COULUMN_SO_TIEN + " =? ",
                new String[] {phieuthus.getSoPT(),phieuthus.getMaKh(), phieuthus.getNgay(),phieuthus.getLoaphieu(), phieuthus.getSotien() + ""},null,null,null);
        if(cursor.moveToFirst()){
            flag = true;
        }

        return flag;
    }


}
