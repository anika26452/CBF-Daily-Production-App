package com.sysnova.cbfdailyproductionapp.Model;

public class ViewBatchListModel {
    String batch, m_batch_id;

    public ViewBatchListModel(String batch, String m_batch_id) {
        this.batch = batch;
        this.m_batch_id = m_batch_id;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getM_batch_id() {
        return m_batch_id;
    }

    public void setM_batch_id(String m_batch_id) {
        this.m_batch_id = m_batch_id;
    }
}
