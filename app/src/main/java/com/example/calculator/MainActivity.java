package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    Button btn1;
    Button btn2;
    Button btn3;
    Button btn4;
    Button btn5;
    Button btn6;
    Button btn7;
    Button btn8;
    Button btn9;
    Button btn0;
    Button btnMul;
    Button btnDiv;
    Button btnMinus;
    Button btnAdd;
    Button btnCE;
    ImageButton btnDel;
    Button btnLeft;
    Button btnRight;
    Button btnDot;
    Button btnEQ;

    TextView txtEquation;
    TextView txtResult;

    private String equation = "";
    private String result = "";

    private int zagrada = 0;

    private ArrayList<Operacije> operacije;

    View appView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        operacije = new ArrayList<Operacije>();

        getSupportActionBar().hide();
        castButtons();
    }

    private void castButtons(){
        btn0 = findViewById(R.id.btn0);
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);
        btn5 = findViewById(R.id.btn5);
        btn6 = findViewById(R.id.btn6);
        btn7 = findViewById(R.id.btn7);
        btn8 = findViewById(R.id.btn8);
        btn9 = findViewById(R.id.btn9);

        btnEQ = findViewById(R.id.btnEq);
        btnDot = findViewById(R.id.btnDot);
        btnAdd = findViewById(R.id.btnAdd);
        btnMinus = findViewById(R.id.btnMinus);
        btnMul = findViewById(R.id.btnMul);
        btnDiv = findViewById(R.id.btnDiv);

        btnCE = findViewById(R.id.btnCE);
        btnDel = findViewById(R.id.btnDel);
        btnLeft = findViewById(R.id.btnLeft);
        btnRight = findViewById(R.id.btnRight);

        txtEquation = findViewById(R.id.txtEquasion);
        txtResult = findViewById(R.id.txtResult);

        btnRight.setEnabled(false);
    }

    public void action0(View view) {
        equation+="0";
        txtEquation.setText(equation);
    }

    public void action1(View view) {
        equation+="1";
        txtEquation.setText(equation);
    }

    public void action2(View view) {
        equation+="2";
        txtEquation.setText(equation);
    }

    public void action3(View view) {
        equation+="3";
        txtEquation.setText(equation);
    }

    public void action4(View view) {
        equation+="4";
        txtEquation.setText(equation);
    }

    public void action5(View view) {
        equation+="5";
        txtEquation.setText(equation);
    }

    public void action6(View view) {
        equation+="6";
        txtEquation.setText(equation);
    }

    public void action7(View view) {
        equation+="7";
        txtEquation.setText(equation);
    }

    public void action8(View view) {
        equation+="8";
        txtEquation.setText(equation);
    }

    public void action9(View view) {
        equation+="9";
        txtEquation.setText(equation);
    }

    public void actionClear(View view){
        equation = "";
        txtEquation.setText(equation);
        txtResult.setText(equation);
    }

    public void actionLeft(View view){
        equation+="(";
        zagrada++;
        txtEquation.setText(equation);

        btnRight.setEnabled(true);
    }

    public void actionRight(View view) {
        equation+=")";
        zagrada--;
        txtEquation.setText(equation);

        if(zagrada == 0) {
            btnRight.setEnabled(false);
        }
    }

    public void actionDiv(View view) {
        equation+="/";
        txtEquation.setText(equation);
    }

    public void actionMul(View view) {
        equation+="*";
        txtEquation.setText(equation);
    }

    public void actionAdd(View view) {
        equation+="+";
        txtEquation.setText(equation);
    }

    public void actionSub(View view) {
        equation+="-";
        txtEquation.setText(equation);
    }

    public void actionDot(View view) {
        boolean dodajNulu = false;

        try {
            char check = equation.charAt(equation.length() - 1);
            if((check == '+') || (check == '-') || (check == '*') || (check == '/')){
                dodajNulu = true;
            }
        }catch (Exception e){
            dodajNulu = true;
        }

        if(dodajNulu){
            equation += "0.";
        }else{
            equation += ".";
        }

        txtEquation.setText(equation);
    }

    public void actionDel(View view) {
        if (equation.length() > 0) {
            char toBeDeleted = equation.charAt(equation.length()-1);

            equation = equation.substring(0, equation.length() - 1);
            txtEquation.setText(equation);
        }
    }

    public void actionCalc(View view) {
        boolean kraj = proveriKraj(equation);
        boolean proveraZagrada = proveriZagrade(equation);

        //dodam u ovu funkciju da ako naidje na *- ili /-, zameni mesta prvom i drugom operandu
        //ako naidje na *+ i /+, samo ukloni plus
        equation = srediNegativneOperacije(equation);
        equation = srediZnakove(equation);

        if(!kraj || !proveraZagrada){
            return;
        }

        boolean zagrade = false;
        int otvorenaZagrada = 0;
        int zatvorenaZagrada = 0;

        for(int i = 0;i < equation.length();i++){
            if(equation.charAt(i) == '('){
                zagrade = true;
                otvorenaZagrada = i;
                break;
            }
        }

        for(int i = equation.length() - 1;i >= 0; i--){
            if(equation.charAt(i) == ')'){
                zatvorenaZagrada = i;
                break;
            }
        }

        String resenje = "";

        if(zagrade){
            equation = resiZagrade(otvorenaZagrada, zatvorenaZagrada, equation);
        }

        resenje = doCalculation(equation);

        resenje = srediResenje(resenje);

        txtResult.setText(resenje);
        if (!resenje.equals("0")) {
            equation = resenje;
        } else {
            equation = "";
        }
    }

    private String srediNegativneOperacije(String equation) {
        boolean menjaj = false;
        for(int i = 0; i < equation.length(); i++){
            if(equation.charAt(i) == '*' || equation.charAt(i) == '/'){
                if(equation.charAt(i+1) == '-' || equation.charAt(i+1) == '+')
                    menjaj = true;
            }

            if(menjaj){
               Character operacija = equation.charAt(i);
               String prvi = equation.substring(0,i);
               String drugi = equation.substring(i+1, equation.length());

               for(int j = prvi.length();j > 0 ;j--){
                   if(!Character.isDigit(prvi.charAt(j-1))){
                       String prviPre = prvi.substring(0,j);
                       String prviPosle = prvi.substring(j,prvi.length());

                       prvi = prviPre + drugi.charAt(0) + prviPosle;
                       drugi = drugi.substring(1, drugi.length());

                       equation = prvi + operacija + drugi;

                       menjaj = false;
                       break;
                   }else{
                       prvi = drugi.charAt(0) + prvi;
                       drugi = drugi.substring(1,drugi.length());

                       equation = prvi + operacija + drugi;

                       menjaj = false;
                       break;
                   }
               }
            }
        }

        return equation;
    }

    private ArrayList<Operacije> izdvojiPrioritetne(ArrayList<Operacije> listaOperacija){
        ArrayList<Operacije> prioritetne = new ArrayList<Operacije>();

        for(int i = 0;i < listaOperacija.size(); i++){
            if(listaOperacija.get(i).getOperacija().equals("*") || listaOperacija.get(i).getOperacija().equals("/")){
                prioritetne.add(listaOperacija.get(i));
            }
        }

        return prioritetne;
    }

    private ArrayList<Operacije> izdvojiOperacije(String equation){
        ArrayList<Operacije> tempOperacije = new ArrayList<Operacije>();
        Operacije o;
        for(int i = 0;i < equation.length();i++){
            o = null;

            if(equation.charAt(i) == '+'){
                o = new Operacije(i, "+");
            }

            if(equation.charAt(i) == '-'){
                o = new Operacije(i, "-");
            }

            if(equation.charAt(i) == '*'){
                o = new Operacije(i, "*");
            }

            if(equation.charAt(i) == '/'){
                o = new Operacije(i, "/");
            }

            if(o != null){
                tempOperacije.add(o);
            }
        }

        return tempOperacije;
    }

    private String srediResenje(String result){
        while(result.endsWith("0") && result.contains(".")){
            result = result.substring(0, result.length() - 1);
            if(result.endsWith(".")){
                result = result.substring(0, result.length() - 1);
            }
        }

        return result;
    }

    private String resiZagrade(int pozicijaOtvorene, int pozicijaZatvorene, String equation){
        String tempEquation = equation.substring(pozicijaOtvorene + 1,pozicijaZatvorene);

        String prvi = equation.substring(0, pozicijaOtvorene);
        String drugi = equation.substring(pozicijaZatvorene + 1, equation.length());

        boolean zagrade = false;
        int otvorenaZagrada = 0;
        int zatvorenaZagrada = 0;

        for(int i = 0;i < tempEquation.length();i++){
            if(tempEquation.charAt(i) == '('){
                zagrade = true;
                otvorenaZagrada = i;
                break;
            }
        }

        for(int i = tempEquation.length() - 1;i >= 0; i--){
            if(tempEquation.charAt(i) == ')'){
                zatvorenaZagrada = i;
                break;
            }
        }

        if(zagrade){
            tempEquation = resiZagrade(otvorenaZagrada, zatvorenaZagrada, tempEquation);
        }

        tempEquation = doCalculation(tempEquation);

        return prvi + tempEquation + drugi;
    }

    private String doCalculation(String forCalculation){
        operacije = izdvojiOperacije(forCalculation);
        ArrayList<Operacije> prioritetne = izdvojiPrioritetne(operacije);
        ArrayList<Boolean> iskoriscene = new ArrayList<Boolean>();

        for(int i = 0;i < prioritetne.size();i++){
            iskoriscene.add(false);
        }

        /////////////////////////////////////////////////////////////////
        for(int i = 0; i < prioritetne.size(); i++){
            //splitujem na znaku, nadjem operande
            String prvi = forCalculation.substring(0, prioritetne.get(i).getPozicija());
            String drugi = forCalculation.substring(prioritetne.get(i).getPozicija() + 1, forCalculation.length());

            int prethodna = -1;
            Operacije zaIzbacivanje = new Operacije();

            for(int j = 0; j < operacije.size(); j++){
                if(operacije.get(j).getPozicija() < prioritetne.get(i).getPozicija()
                        && !iskoriscene.get(i)){
                    prethodna = operacije.get(j).getPozicija();
                    zaIzbacivanje = operacije.get(j+1);
                }else{
                    break;
                }
            }

            Operacije o = new Operacije(0,"+");
            int sledeci = 0;
            boolean poslednji = true;

            for(int j = 0; j < operacije.size();j++){
                if(operacije.get(j).getPozicija() > prioritetne.get(i).getPozicija()){
                    poslednji = false;
                    break;
                }
            }

            if(!poslednji) {
                for (int j = 0; j < operacije.size(); j++) {
                    if (operacije.get(j).getPozicija() == prioritetne.get(i).getPozicija()) {
                        o = operacije.get(j + 1);
                        sledeci = o.getPozicija() - prvi.length() - 1;
                    }
                }
            }else{
                sledeci = forCalculation.length() - prvi.length() - 1;
            }

            //substringujem i izvucem operande
            float prviOperand = 0;
            float drugiOperand = 0;

            /////////////////////////////////////////////
            String tempPrvi = "";

            if(prethodna != -1){
                tempPrvi = prvi.substring(prethodna + 1, prvi.length());
            }else{
                tempPrvi = prvi.substring(0, prvi.length());
            }
            prviOperand = Float.parseFloat(tempPrvi);

            //////////////////////////////////////////////
            String tempDrugi = drugi.substring(0,sledeci);
            drugiOperand = Float.parseFloat(tempDrugi);

            //pomnozim i vratim u string

            BigDecimal rezultat = new BigDecimal(prviOperand);

            if(prioritetne.get(i).getOperacija().equals("*")){
                rezultat = (new BigDecimal(prviOperand).multiply(new BigDecimal(drugiOperand)));
            }else{
                Float rez = prviOperand / drugiOperand;
                rezultat = new BigDecimal(rez);
                rezultat = rezultat.round(MathContext.DECIMAL32);
            }

            String stringRezultat = String.valueOf(rezultat);

            for(int j = 0;j < operacije.size();j++){
                if(operacije.get(j).getPozicija() > prioritetne.get(i).getPozicija()){
                    int temp = operacije.get(j).getPozicija();

                    if(prethodna == -1){
                        prethodna = 0;
                    }

                    String tempString = "";
                    if(prethodna != 0) {
                        tempString = forCalculation.substring(prethodna + 1, sledeci + 1 + prvi.length());
                    }else{
                        tempString = forCalculation.substring(prethodna, sledeci + 1 + prvi.length());
                    }
                    operacije.get(j).setPozicija(temp + stringRezultat.length() - tempString.length());
                }
            }
            //Ovde pokusavam da apdejtujem indekse operacija
            if(prethodna != -1){
                forCalculation = prvi.substring(0, prethodna + 1) +
                        stringRezultat;
                if(!drugi.equals("")) {
                    String drugiDeo = drugi.substring(sledeci, drugi.length());
                    forCalculation += drugiDeo;
                }
            }else{
                forCalculation = stringRezultat + drugi.substring(sledeci, drugi.length());
            }

            iskoriscene.set(i, true);
            prioritetne.set(0, new Operacije(0,"*"));

            operacije.remove(zaIzbacivanje);
        }
        /////////

        boolean sabiranje = forCalculation.contains("+");
        boolean oduzimanje = forCalculation.contains("-");

        float prvi = 0;

        if(sabiranje || oduzimanje){
            if((forCalculation.charAt(0) == '-' || forCalculation.charAt(0) == '+')
                && operacije.size() < 2){
                return forCalculation;
            }

            prvi = Float.parseFloat(forCalculation.substring(0, operacije.get(0).getPozicija()));
        }else{
            prvi = Float.parseFloat(forCalculation);
        }

        BigDecimal d = new BigDecimal(prvi);
        for(int i = 0;i < operacije.size();i++){
            double drugi = 0;
            try{
                drugi = Double.parseDouble(forCalculation.substring(operacije.get(i).getPozicija() + 1,operacije.get(i+1).getPozicija()));
            }catch(Exception e) {
                try{
                    drugi = Double.parseDouble(forCalculation.substring(operacije.get(i).getPozicija() + 1, forCalculation.length()));
                } catch(Exception e1){
                    break;
                }
            }

            if(operacije.get(i).getOperacija().equals("+")){
                d = ((d.add((new BigDecimal(drugi)))));
            }else if(operacije.get(i).getOperacija().equals("-")) {
                d = (d.subtract((new BigDecimal(drugi))));
            }
        }
        d = d.round(MathContext.DECIMAL32);
        DecimalFormat dm = new DecimalFormat("#.#####");
        result = dm.format(d);

        operacije.clear();

        return result;
    }



    private boolean proveriKraj(String checkString){

        char sign = checkString.charAt(checkString.length()-1);

        if((sign == '+') || (sign == '-') || (sign == '*') || (sign == '/')){
            return false;
        }

        return true;
    }

    private boolean proveriZagrade(String checkString){
        int brojOtvorenih = 0;
        int brojZatvorenih = 0;
        for(int i = 0;i < checkString.length();i++){
            if(checkString.charAt(i) == '('){
                brojOtvorenih++;
            }

            if(checkString.charAt(i) == ')'){
                brojZatvorenih++;
            }
        }

        if(brojOtvorenih == brojZatvorenih){
            return true;
        }

        return false;
    }

    private String srediZnakove(String oldEquation){
        String newEquation = oldEquation;

        for(int i = 0;i < newEquation.length();i++){
            if(newEquation.charAt(i) == '+'){
                if(newEquation.charAt(i+1) == '-'){
                    String prvi = newEquation.substring(0, i);
                    String drugi = newEquation.substring(i + 2, newEquation.length());

                    newEquation = prvi + "-" + drugi;
                }
            }
        }

        for(int i = 0;i < newEquation.length();i++){
            if(newEquation.charAt(i) == '-'){
                if(newEquation.charAt(i+1) == '+'){
                    String prvi = newEquation.substring(0, i);
                    String drugi = newEquation.substring(i + 2, newEquation.length());

                    newEquation = prvi + "-" + drugi;
                }
            }
        }

        for(int i = 0;i < newEquation.length();i++){
            if(newEquation.charAt(i) == '-'){
                if(newEquation.charAt(i+1) == '-'){
                    String prvi = newEquation.substring(0, i);
                    String drugi = newEquation.substring(i + 2, newEquation.length());

                    newEquation = prvi + "+" + drugi;
                }
            }
        }

        for(int i = 0;i < newEquation.length();i++){
            if(newEquation.charAt(i) == '+'){
                if(newEquation.charAt(i+1) == '+'){
                    String prvi = newEquation.substring(0, i);
                    String drugi = newEquation.substring(i + 2, newEquation.length());

                    newEquation = prvi + "+" + drugi;
                }
            }
        }

        return newEquation;
    }
}
