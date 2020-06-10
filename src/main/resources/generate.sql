CREATE TABLE "game" (
	"id" serial NOT NULL,
	"start_date" TIMESTAMP,
	"end_date" TIMESTAMP,
	"act_date" TIMESTAMP,
	"current_team_id" INT,
	CONSTRAINT game_pk PRIMARY KEY ("id")
) WITH (
    OIDS=FALSE
);

CREATE TABLE "team" (
	"id" serial NOT NULL,
	"name" TEXT NOT NULL,
	"score" INT NOT NULL,
	"game_id" INT NOT NULL,
	"current_player_id" INT,
	"is_lobby" BOOLEAN NOT NULL DEFAULT false,
	CONSTRAINT result_pk PRIMARY KEY ("id")
) WITH (
    OIDS=FALSE
);

CREATE TABLE "player" (
	"id" serial NOT NULL,
	"name" TEXT NOT NULL,
	"hash" TEXT NOT NULL,
	"team_id" INT NOT NULL,
	CONSTRAINT player_pk PRIMARY KEY ("id")
) WITH (
    OIDS=FALSE
);

CREATE TABLE "word" (
	"id" serial NOT NULL,
	"word" TEXT NOT NULL,
	CONSTRAINT word_pk PRIMARY KEY ("id")
) WITH (
    OIDS=FALSE
);

CREATE TABLE "word_used" (
	"game_id" int NOT NULL,
	"word_id" int NOT NULL,
	CONSTRAINT word_used_pk PRIMARY KEY ("game_id", "word_id")
) WITH (
    OIDS=FALSE
);

CREATE TABLE "admin" (
    "id" serial NOT NULL,
    "login" TEXT NOT NULL UNIQUE,
    "pwd_hash" TEXT NOT NULL,
    CONSTRAINT admin_pk PRIMARY KEY ("id")
) WITH (
    OIDS=FALSE
);


ALTER TABLE "game" ADD CONSTRAINT "game_fk0" FOREIGN KEY ("current_team_id") REFERENCES "team"("id");
ALTER TABLE "team" ADD CONSTRAINT "team_fk0" FOREIGN KEY ("game_id") REFERENCES "game"("id");
ALTER TABLE "team" ADD CONSTRAINT "team_fk1" FOREIGN KEY ("current_player_id") REFERENCES "player"("id");
ALTER TABLE "player" ADD CONSTRAINT "player_fk0" FOREIGN KEY ("team_id") REFERENCES "team"("id");
ALTER TABLE "word_used" ADD CONSTRAINT "word_used_fk0" FOREIGN KEY ("game_id") REFERENCES "game"("id");
ALTER TABLE "word_used" ADD CONSTRAINT "word_used_fk1" FOREIGN KEY ("word_id") REFERENCES "word"("id");
