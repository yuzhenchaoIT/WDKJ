package com.wd.tech.bean.details;

import java.util.Map;

/**
 * date:2019/3/3
 * author:李阔(淡意衬优柔)
 * function:
 */
public class PayResult {
    private String result;
    private String resultStatus;

    public PayResult(Map<String, String> obj) {

    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(String resultStatus) {
        this.resultStatus = resultStatus;
    }
}
