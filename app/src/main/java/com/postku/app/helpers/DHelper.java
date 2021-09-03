package com.postku.app.helpers;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DHelper {
    public static Double d(String s){
        Double localDouble1 = Double.valueOf(0.0D);
        try {
            Double localDouble2 = Double.valueOf(Double.parseDouble(s));
            return localDouble2;
        } catch (Exception localException) {
        }
        return localDouble1;
    }

    public static void pesan(final Context context, String msg){
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static String toformatRupiah(String s){
        Locale localeID = new Locale("in", "ID");
        DecimalFormat decimalFormat = (DecimalFormat) DecimalFormat.getCurrencyInstance(localeID);
        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
        decimalFormatSymbols.setCurrencySymbol("");
        decimalFormatSymbols.setMonetaryDecimalSeparator(',');
        decimalFormatSymbols.setGroupingSeparator('.');
        decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);
        decimalFormat.setMaximumFractionDigits(0);
        return decimalFormat.format(d(s)).replace(",", ".");
    }

    public static boolean isValidEmail(EditText email) {
        boolean isValid = false;
        String expresion = "^[\\\\w\\\\.-]+@([\\\\w\\\\-]+\\\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email.getText().toString();

        Pattern pattern = Pattern.compile(expresion, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()){
            isValid = true;
        }
        return isValid;
    }

    public static void isLoading(boolean load, TextView textView, ProgressBar progressBar){
        if(load){
            textView.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }else {
            textView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
    }

    public static boolean isCompare(EditText etText, EditText ex){
        String a = etText.getText().toString();
        String b = ex.getText().toString();
        return !a.equals(b);
    }

    public static Date strToDateFull(String data) {
        DateFormat df = new SimpleDateFormat("EEEE, dd MMM yyyy", Locale.getDefault());
        Date startDate = null;
        String newDateString = "";
        try {
            startDate = df.parse(data);
            // newDateString = df.format(startDate);
            System.out.println("newdate : " + startDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return startDate;
    }
    public static Date dateAddYear(Date in, int tahun) {
        if (in == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(in);
        cal.add(Calendar.YEAR, tahun);
        return cal.getTime();

    }
    public static String tglSekarang() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static Date strTodate(String data) {
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Date startDate = null;
        String newDateString = "";
        try {
            startDate = df.parse(data);
            // newDateString = df.format(startDate);
            System.out.println("newdate : " + startDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return startDate;
    }

    public static boolean isBatasUsia(String etText, int batas) {
//        String a = etText.getText().toString();

        Date usia = strTodate(etText);
        Date sekarang = dateAddYear(strTodate(tglSekarang()), batas);
        SimpleDateFormat simpleDate =  new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        if (usia.before(sekarang)) {
            return false;
        } else {
            return true;
        }

    }


}
