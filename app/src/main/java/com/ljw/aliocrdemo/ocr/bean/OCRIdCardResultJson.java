package com.ljw.aliocrdemo.ocr.bean;

import java.util.List;

/**
 *
 * @author Android(JiaWei)
 * @date 2017/8/24
 */

public class OCRIdCardResultJson {
    private List<OCROutputs> outputs;

    public List<OCROutputs> getOutputs() {
        return outputs;
    }

    public void setOutputs(List<OCROutputs> outputs) {
        this.outputs = outputs;
    }

    public static class OCROutputs {
        private String outputLabel;
        private OCROutputMulti outputMulti;
        private OCROutputValue outputValue;

        public String getOutputLabel() {
            return outputLabel;
        }

        public void setOutputLabel(String outputLabel) {
            this.outputLabel = outputLabel;
        }

        public OCROutputMulti getOutputMulti() {
            return outputMulti;
        }

        public void setOutputMulti(OCROutputMulti outputMulti) {
            this.outputMulti = outputMulti;
        }

        public OCROutputValue getOutputValue() {
            return outputValue;
        }

        public void setOutputValue(OCROutputValue outputValue) {
            this.outputValue = outputValue;
        }
    }

    public static class OCROutputMulti {
    }

    public static class OCROutputValue {
        private int dataType;
        private String dataValue;

        public int getDataType() {
            return dataType;
        }

        public void setDataType(int dataType) {
            this.dataType = dataType;
        }

        public String getDataValue() {
            return dataValue;
        }

        public void setDataValue(String dataValue) {
            this.dataValue = dataValue;
        }
    }
}
