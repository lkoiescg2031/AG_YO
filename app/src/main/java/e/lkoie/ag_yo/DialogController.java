package e.lkoie.ag_yo;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by 김태훈 on 2017-12-08.
 */

public class DialogController {
    
    private Context context;
    private View dialogView;
    private EditText editNum, editQuiz, editAns;
    private TextView txtQuiz, txtAns;
    private DBController dbController;
    private boolean flag;
    private int correctCnt,quizNum;
    private ArrayList<Integer> proNumList;

    public void showPlayDialog(int quizNum){
        dbController.openRead();
        proNumList = dbController.selectProblemNumber();
        dbController.close();
        Collections.shuffle(proNumList);
        this.quizNum = quizNum < proNumList.size() ? quizNum : proNumList.size();
        this.quizNum--;
        correctCnt = 0;
        if(this.quizNum >= 0)
            showQuizPlayDialog(0);
        else
            Toast.makeText(context, "퀴즈가 없습니다.", Toast.LENGTH_SHORT).show();
    }

    //문제를 보여주는 메서드
    private boolean showQuizPlayDialog(final int index){

        dbController.openRead();
        final Pair<String,String> problem = dbController.selectProblem(proNumList.get(index));
        dbController.close();
        dialogView = (View) View.inflate(context,R.layout.dialog_play,null);
        TextView txt= (TextView) dialogView.findViewById(R.id.txtQuiz);
        editAns = (EditText) dialogView.findViewById(R.id.dialog_paly_edtAnswer);
        txt.setText(problem.first);

        flag = false;
        //문제를 보여주는 다이얼 로그를 호출
        new AlertDialog.Builder(context).setTitle("Quiz").setView(dialogView).setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(editAns.getText().toString().equals(problem.second)){//문제가 정답인지 검사
                    //정답인 경우 DB를 열어 풀었다고 알려줌
                    dbController.openRead().changeToSolveProblem(proNumList.get(index));
                    correctCnt++;
                    dbController.close();
                }
                if(index < quizNum)//현재 문항 수가 10가지 이하이면
                    showQuizPlayDialog(index + 1);//재귀 호출
                else//아닌경우
                    showQuizResultDialog(correctCnt);//결과 다이얼로그 호출
            }
        }).show();
        return flag;
    }
    //문제 맞춘수를 보여주는 메서드
    private void showQuizResultDialog(int number){//정답 문항 수를 입력받아
        dialogView = (View) View.inflate(context,R.layout.dialog_result,null);
        TextView txt= (TextView) dialogView.findViewById(R.id.dialog_result_result);
        txt.setText(String.valueOf(number));//정답 문항 수를 출력
        new AlertDialog.Builder(context).setTitle("Quiz").setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //확인 버튼을 누르면
                refreshActivity();//엑티비티를 다시 호출함
            }
        }).setView(dialogView).show();//다이얼 로그를 출력
    }
    //문제를 삽입하는 메서드
    public void insertQuizDialog(){
        dialogView = (View) View.inflate(context,R.layout.dialog_insert,null);
        editQuiz = (EditText) dialogView.findViewById(R.id.dialog_insert_editQuiz);
        editAns = (EditText) dialogView.findViewById(R.id.dialog_insert_editAns);
        new AlertDialog.Builder(context).setTitle("새로운 Quiz").setNegativeButton("취소",null)
                .setView(dialogView).setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //삽입하는 쿼리
                dbController.openWrite();
                dbController.addProblem(editQuiz.getText().toString(),editAns.getText().toString());
                dbController.close();
                refreshActivity();
            }
        }).show();
    }
    //문제를 업데이트하는 메서드
    public void updateQuizDialog(){
        dialogView = (View) View.inflate(context,R.layout.dialog_update,null);
        TextView txt= (TextView) dialogView.findViewById(R.id.txtQuiz);
        editNum = (EditText) dialogView.findViewById(R.id.dialog_update_editNum);
        editNum.addTextChangedListener(new TextWatcher() {
            @Override//TextchangedListener를 호출
            public void afterTextChanged(Editable s) {
                try{//Number의 Text를 읽어옴
                    int i = Integer.parseInt(editNum.getText().toString());
                    //DB를 열어 정보를 호출
                    dbController.openRead();
                    Pair<String,String> problem = dbController.selectProblem(i);
                    //Edit을 갱신함
                    editQuiz.setText(problem.first);
                    editAns.setText(problem.second);
                }catch (Exception e){//실패한 경우 Text를 초기화 함
                    e.printStackTrace();
                    editAns.setText(null);
                    editQuiz.setText(null);
                }
                dbController.close();
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

        });
        editQuiz = (EditText) dialogView.findViewById(R.id.dialog_update_editQuiz);
        editAns = (EditText) dialogView.findViewById(R.id.dialog_update_editAns);
        new AlertDialog.Builder(context).setTitle("업데이트 Quiz").setNegativeButton("취소",null)
                .setView(dialogView).setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //업데이트
                dbController.openWrite();
                dbController.updateProblem(Integer.parseInt(editNum.getText().toString()),editQuiz.getText().toString(),editAns.getText().toString());
                dbController.close();
                refreshActivity();
            }
        }).show();
    }
    //문제를 삭제하는 메서드
    public void deleteQuizDialog(){
        dialogView = (View) View.inflate(context,R.layout.dialog_delete,null);
        txtQuiz = (TextView) dialogView.findViewById(R.id.dialog_delete_txtQuiz);
        txtAns = (TextView) dialogView.findViewById(R.id.dialog_delete_txtAns);
        editNum = (EditText) dialogView.findViewById(R.id.dialog_delete_editNum);
        editNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                try{
                    int num = Integer.parseInt(editNum.getText().toString());
                    dbController.openRead();
                    Pair<String,String> problem = dbController.selectProblem(num);
                    txtQuiz.setText(problem.first);
                    txtAns.setText(problem.second);
                }catch (Exception e){
                    txtQuiz.setText(null);
                    txtAns.setText(null);
                }
                dbController.close();
            }
        });
        new AlertDialog.Builder(context).setTitle("재미없는 Quiz").setNegativeButton("취소",null)
                .setView(dialogView).setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //삭제 쿼리
                try{
                    dbController.openWrite();
                    dbController.deleteProblem(Integer.parseInt(editNum.getText().toString()));
                    refreshActivity();
                }catch (Exception e){
                }
                dbController.close();
            }
        }).show();
    }

    //constructor
    public DialogController(Context context) {
        this.context = context;
        dbController = new DBController(context);
    }
    //getter
    public EditText getEditNum() {
        return editNum;
    }

    public EditText getEditQuiz() {
        return editQuiz;
    }

    public EditText getEditAns() {
        return editAns;
    }

    private void refreshActivity(){
        Intent intent = new Intent(context,JokeListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        ((Activity)context).startActivity(intent);
        ((Activity)context).finish();
    }
}
