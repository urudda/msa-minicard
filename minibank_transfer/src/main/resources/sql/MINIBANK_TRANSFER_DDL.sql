CREATE TABLE TB_TRNF_HST
(
  CSTM_ID	VARCHAR(20) NOT NULL,
  SEQ		INTEGER NOT NULL,
  DIV_CD		VARCHAR(1) NOT NULL,
  STS_CD  	VARCHAR(1) NOT NULL,
  DPST_ACNT_NO	VARCHAR(20) NOT NULL,
  WTHD_ACNT_NO	VARCHAR(20) NOT NULL,
  SND_MM	VARCHAR(100) NOT NULL,
  RCV_MM	VARCHAR(100) NOT NULL,
  RCV_CSTM_NM	VARCHAR(20) NOT NULL,
  TRNF_AMT	BIGINT DEFAULT 0,
  TRNF_DTM	VARCHAR(20) NOT NULL
)
;
COMMENT ON TABLE TB_TRNF_HST
  IS '이체내역';
COMMENT ON COLUMN TB_TRNF_HST.CSTM_ID
  IS '고객ID';
COMMENT ON COLUMN TB_TRNF_HST.SEQ
  IS '순번';
COMMENT ON COLUMN TB_TRNF_HST.DIV_CD
  IS '구분코드';
COMMENT ON COLUMN TB_TRNF_HST.STS_CD
  IS '상태코드';
COMMENT ON COLUMN TB_TRNF_HST.DPST_ACNT_NO
  IS '입금계좌번호';
COMMENT ON COLUMN TB_TRNF_HST.WTHD_ACNT_NO
  IS '출금계좌번호';
COMMENT ON COLUMN TB_TRNF_HST.SND_MM
  IS '내통장메모';
COMMENT ON COLUMN TB_TRNF_HST.RCV_MM
  IS '받는통장메모';
COMMENT ON COLUMN TB_TRNF_HST.RCV_CSTM_NM
  IS '받는고객명';
COMMENT ON COLUMN TB_TRNF_HST.TRNF_AMT
  IS '이체금액';
COMMENT ON COLUMN TB_TRNF_HST.TRNF_DTM
  IS '이체일시';
ALTER TABLE TB_TRNF_HST
  ADD CONSTRAINT TB_TRNF_HST_PK PRIMARY KEY (CSTM_ID, SEQ);

CREATE TABLE TB_TRNF_LMT
(
  CSTM_ID		VARCHAR(20) NOT NULL,
  ONE_TM_TRNF_LMT	BIGINT DEFAULT 0,
  ONE_DY_TRNF_LMT	BIGINT DEFAULT 0
)
;
COMMENT ON TABLE TB_TRNF_LMT
  IS '이체한도';
COMMENT ON COLUMN TB_TRNF_LMT.CSTM_ID
  IS '고객ID';
COMMENT ON COLUMN TB_TRNF_LMT.ONE_TM_TRNF_LMT
  IS '1회이체한도';
COMMENT ON COLUMN TB_TRNF_LMT.ONE_DY_TRNF_LMT
  IS '1일이체한도';
ALTER TABLE TB_TRNF_LMT
  ADD CONSTRAINT TB_TRNF_LMT_PK PRIMARY KEY (CSTM_ID);