CREATE TABLE "game" (
	"id" serial NOT NULL,
	"start_date" TIMESTAMP,
	"end_date" TIMESTAMP,
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
	CONSTRAINT result_pk PRIMARY KEY ("id")
) WITH (
    OIDS=FALSE
);

CREATE TABLE "player" (
	"id" serial NOT NULL,
	"name" TEXT NOT NULL,
	"team_id" INT NOT NULL,
	CONSTRAINT player_pk PRIMARY KEY ("id")
) WITH (
    OIDS=FALSE
);



ALTER TABLE "game" ADD CONSTRAINT "game_fk0" FOREIGN KEY ("current_team_id") REFERENCES "team"("id");
ALTER TABLE "team" ADD CONSTRAINT "team_fk0" FOREIGN KEY ("game_id") REFERENCES "game"("id");
ALTER TABLE "team" ADD CONSTRAINT "team_fk1" FOREIGN KEY ("current_player_id") REFERENCES "player"("id");
ALTER TABLE "player" ADD CONSTRAINT "player_fk0" FOREIGN KEY ("team_id") REFERENCES "team"("id");
