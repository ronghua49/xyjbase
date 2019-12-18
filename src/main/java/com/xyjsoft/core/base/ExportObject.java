package com.xyjsoft.core.base;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 类名:ExportObject
 * 类描述:TODO
 *
 * @author gsh456
 * @version 1.0
 * @date 2019-04-22 11:16
 * @since JDK1.8
 */

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class ExportObject {
    private Double s1;
    private Double s2;
    private Double s3;
    private Double s4;
    private Double s5;

    public Double getS1() {
        return s1;
    }

    public void setS1(Double s1) {
        this.s1 = s1;
    }

    public Double getS2() {
        return s2;
    }

    public void setS2(Double s2) {
        this.s2 = s2;
    }

    public Double getS3() {
        return s3;
    }

    public void setS3(Double s3) {
        this.s3 = s3;
    }

    public Double getS4() {
        return s4;
    }

    public void setS4(Double s4) {
        this.s4 = s4;
    }

    public Double getS5() {
        return s5;
    }

    public void setS5(Double s5) {
        this.s5 = s5;
    }
}
