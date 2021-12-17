DROP DATABASE IF EXISTS line_function_db;

CREATE DATABASE line_function_db;

\c line_function_db

DROP TABLE IF EXISTS excel_file CASCADE;

CREATE TABLE IF NOT EXISTS excel_file
(
    id  serial  PRIMARY KEY,
    excel_file_name text NOT NULL,
    spreadsheet_number  int NOT NULL
);

DROP TABLE IF EXISTS spreadsheet CASCADE;

CREATE TABLE IF NOT EXISTS spreadsheet
(
    id                serial PRIMARY KEY,
    excel_file_id   int NOT NULL,
    spread_sheet_name text NOT NULL,
    has_annotated    bool    NOT NULL DEFAULT FALSE,
    CONSTRAINT excel_file_id_fk FOREIGN KEY (excel_file_id)
        REFERENCES excel_file (id) MATCH SIMPLE
        ON UPDATE CASCADE ON DELETE CASCADE
);

DROP TABLE IF EXISTS annotation_time_cost CASCADE;

CREATE TABLE IF NOT EXISTS annotation_time_cost
(
    id  serial PRIMARY KEY,
    spreadsheet_id  int NOT NULL,
    time_cost   int    NOT NULL,
    CONSTRAINT annotation_time_cost_spreadsheet_id_fk FOREIGN KEY (spreadsheet_id)
        REFERENCES spreadsheet (id) MATCH SIMPLE
        ON UPDATE CASCADE ON DELETE CASCADE
);

DROP TABLE IF EXISTS line_function CASCADE;

CREATE TABLE IF NOT EXISTS line_function
(
    id                serial PRIMARY KEY,
    spreadsheet_id      int     NOT NULL,
    line_number int NOT NULL,
    line_type         text NOT NULL,
    CONSTRAINT data_file_id_fk FOREIGN KEY (spreadsheet_id)
        REFERENCES spreadsheet (id) MATCH SIMPLE
        ON UPDATE CASCADE ON DELETE CASCADE
);