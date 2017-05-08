package com.example.bm_admin.timekeeper.bean;

/**
 * Created by bm-admin on 7/4/17.
 */
public class PuzzleBean {
    private String puzz_id;
    private String puzz_quiz;
    private String puzz_option1;
    private String puzz_option2;
    private String puzz_option3;
    private String puzz_option4;
    private String puzz_option5;

    public PuzzleBean(String puzz_id, String puzz_quiz, String puzz_option1, String puzz_option2,
                      String puzz_option3, String puzz_option4) {
        this.puzz_id = puzz_id;
        this.puzz_quiz = puzz_quiz;
        this.puzz_option1 = puzz_option1;
        this.puzz_option2 = puzz_option2;
        this.puzz_option3 = puzz_option3;
        this.puzz_option4 = puzz_option4;
    }

    public String getPuzz_id() {
        return puzz_id;
    }

    public void setPuzz_id(String puzz_id) {
        this.puzz_id = puzz_id;
    }

    public String getPuzz_quiz() {
        return puzz_quiz;
    }

    public void setPuzz_quiz(String puzz_quiz) {
        this.puzz_quiz = puzz_quiz;
    }

    public String getPuzz_option1() {
        return puzz_option1;
    }

    public void setPuzz_option1(String puzz_option1) {
        this.puzz_option1 = puzz_option1;
    }

    public String getPuzz_option2() {
        return puzz_option2;
    }

    public void setPuzz_option2(String puzz_option2) {
        this.puzz_option2 = puzz_option2;
    }

    public String getPuzz_option3() {
        return puzz_option3;
    }

    public void setPuzz_option3(String puzz_option3) {
        this.puzz_option3 = puzz_option3;
    }

    public String getPuzz_option4() {
        return puzz_option4;
    }

    public void setPuzz_option4(String puzz_option4) {
        this.puzz_option4 = puzz_option4;
    }
}
