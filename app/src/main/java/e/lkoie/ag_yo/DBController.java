package e.lkoie.ag_yo;

/**
 * Created by kkprs on 2017-12-08.
 */

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.util.Pair;

import java.util.ArrayList;

public class DBController extends SQLiteOpenHelper {

    private Context context;
    private SQLiteDatabase sqlDB;
    private Integer  number;
    //DB 오픈
    public DBController openWrite() {
        sqlDB = getWritableDatabase();
        return this;
    }
    public DBController openRead(){
        sqlDB = getReadableDatabase();
        return this;
    }
    //InsertQuery
    //문제 삽입
    public void addProblem(String problem, String answer){
        try {
            number = getProblemNumber();
            sqlDB.execSQL("INSERT INTO problem VALUES(" + number + ", '" + problem + "', '" + answer + "', 0);");
        }catch (Exception e){
            Log.e("addProblem Error",e.getMessage());
        }
    }
    //DeleterQuery
    //삭제
    public boolean deleteProblem(Integer problemNumber){
        boolean result = false;
        try{
            sqlDB.execSQL("DELETE FROM problem WHERE problemNumber = " + problemNumber +";");
            result = true;
        }catch (Exception e){
            Log.e("deleteProblem Error",e.getMessage());
        }
        return result;
    }
    //UpdateQuery
    //수정
    public boolean updateProblem(Integer problemNumber, String problem, String answer){
        boolean result = false;
        try{
            if(!problem.equals(""))
                sqlDB.execSQL("UPDATE problem SET problemName = " + problem + " WHERE problemNumber = " + problemNumber + ";");
            if(!answer.equals(""))
                sqlDB.execSQL("UPDATE problem SET answer = " + answer + " WHERE problemNumber = " + problemNumber + ";");
            result = true;
        }catch (Exception e){
            Log.e("updateProblem Error",e.getMessage());
        }
        return result;
    }
    //selectQuery
    //문제 하나 검색
    public Pair<String, String> selectProblem(Integer problemNumber){
        String problem = null,answer = null;
        Cursor cursor;
        cursor = sqlDB.rawQuery("SELECT problemName, answer FROM problem WHERE problemNumber = "+problemNumber+";",null);
        while(cursor.moveToNext()){
            problem = cursor.getString(0);
            answer = cursor.getString(1);
        }
        return new Pair<>(problem, answer);
    }
    //모든 푼 문제 검색
    public ArrayList<String> selectSolveProblem(){
        ArrayList <String> solveProblem = new ArrayList<String>();
        try{
            Cursor cursor;
            cursor = sqlDB.rawQuery("SELECT * FROM problem WHERE solveFlag = 1;",null);
            while(cursor.moveToNext())
                solveProblem.add(cursor.getString(0)+". "+cursor.getString(1));
            cursor.close();
        }catch (Exception e){
            Log.e("selectSolPro error",e.getMessage());
        }
        return solveProblem;
    }
    //모든 안 푼 문제 검색
    public ArrayList<String> selectUnsolveProblem(){
        ArrayList <String> unsolvedProblem = new ArrayList<String>();
        try{
            Cursor cursor;
            cursor = sqlDB.rawQuery("SELECT * FROM problem WHERE solveFlag = 0;",null);
            while(cursor.moveToNext())
                unsolvedProblem.add(cursor.getString(0)+". "+cursor.getString(1));
            cursor.close();
        }catch (Exception e){
            Log.e("selectUnsolPro Error",e.getMessage());
        }
        return unsolvedProblem;
    }
    //안 푼 문제 -> 푼 문제로 변경
    public boolean changeToSolveProblem(int problemNumber){
        boolean result = false;
        try{
            sqlDB.execSQL("UPDATE problem SET solveFlag = 1 WHERE problemNumber = " + problemNumber + ";");
            result = true;
        }catch (Exception e){
            Log.e("changeToSolPro Error",e.getMessage());
        }
        return result;
    }
    //비어있는 문제 번호 찾기
    private int getProblemNumber(){
        number = 0;
        Integer currentNumber = 0;
        try {
            Cursor cursor;
            cursor = sqlDB.rawQuery("SELECT problemNumber FROM problem;", null);
            while (cursor.moveToNext()) {
                currentNumber = cursor.getInt(0);
                if (currentNumber - number > 1)
                    break;
                number = currentNumber;
            }
            cursor.close();
        }catch (Exception e){
            Log.e("getProNum Error",e.getMessage());
        }
        number++;
        return number;
    }
    //모든 문제번호찾기
    public ArrayList<Integer> selectProblemNumber(){
        ArrayList<Integer> result = new ArrayList<>();
        try {
            Cursor cursor;
            cursor = sqlDB.rawQuery("SELECT * FROM problem;", null);
            while (cursor.moveToNext())
                result.add(cursor.getInt(0));
            cursor.close();
        }catch (Exception e){
            Log.e("selectProNum Error",e.getMessage());
        }
        return result;
    }
    //Constructor
    public DBController(Context context) {
        super(context,"problem",null,1);
        this.context  = context;
    }
    //DB 테이블 초기화 후 생성
    public void createTable(int version){
        try{
            onUpgrade(sqlDB,0,1);
        }catch (Exception e){
            Log.e("createTable Error",e.getMessage());
        }
    }
    //초기테이블 생성 후 값 추가;
    public void initialTable(String problemList){
        try {
            String[] problem = problemList.split("_");
            for(int i=0;i<problem.length;i=i+2){
                problem[i] = delNumber(problem[i]);
                addProblem(problem[i],problem[i+1]);
            }
        }
        catch (Exception e){
        }
    }
    //테이블 생성
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE problem (problemNumber INTEGER PRIMARY KEY not null, problemName CHAR(200) not null, answer CHAR(200) not null, solveFlag INTEGER not null);");
    }
    //테이블 존재할 경우 기존의 테이블 삭제 시키고 새로 테이블 생성
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS problem");
        onCreate(sqLiteDatabase);
    }
    //문자열에서 번호 지우기
    private String delNumber(String string){
        String result = null;
        int i = 0;
        try{
            for(;Integer.parseInt(string.substring(i,i+1)) >=0 && i<string.length();i++);
        }catch (Exception e){
            result = string.substring(i+1);
        }
        return result;
    }
}
