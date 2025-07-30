package com.capstone.ads.constaint;

public class S3ImageKeyFormat {
    public static final String BACKGROUND = "background/%s/%s";
    public static final String CONTRACT = "contract/%s/%s";
    public static final String FINAL_CUSTOM_DESIGN = "custom-design/%s/final";
    public static final String FINAL_CUSTOM_DESIGN_SUB_IMAGE = "custom-design/%s/sub-final/%s";
    public static final String DEMO_DESIGN = "custom-design/%s/%s";
    public static final String DEMO_DESIGN_SUB_IMAGE = "custom-design/%s/sub-demo/%s";
    public static final String DESIGN_TEMPLATE = "design-template/%s/%s";
    public static final String EDITED_DESIGN = "edited-design/%s/%s";
    public static final String FEEDBACK = "feedback/%s/%s";
    public static final String ICON = "icon/%s";
    public static final String FINE_TUNE_FILE = "uploadJsonlFile/%s_%s";
    public static final String PRODUCT_TYPE = "product-type/%s";
    public static final String ORDER_LOG = "orders/%s/%s/%s";
    public static final String AVATAR = "avatar/%s";
    public static final String CONTRACTOR = "contractor/%s";

    private S3ImageKeyFormat() {
    }
}
