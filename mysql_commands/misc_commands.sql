-- SELECT * FROM accounts;
-- INSERT INTO accounts VALUES("6362364517", "luohugu", "1998-05-11");

-- SELECT * FROM emergencyContacts;

-- SELECT * FROM teams;
-- INSERT INTO teams (tname, description) VALUES ("Facebook", "everyone hates Facebook");
-- SELECT MAX(numMembers), tName, description FROM teams;
-- SELECT tid FROM members WHERE phone = "3149607356";

-- SELECT * FROM members;
-- INSERT INTO members VALUES("3149607356", 1);
-- UPDATE teams SET numMembers = 2 WHERE tid = 3;

-- SELECT * FROM games;
-- INSERT INTO games (sport, description, gameDate, startTime, endTime, minAge, maxAge, minSkillLevel, capacity, creator)
-- VALUES("Tennis", "test", "2018-05-11", "14:30", "15:30", 15, 30, 2, 2, "3149607356");
-- UPDATE games SET attendees = attendees + 1 WHERE gid IN (1, 2, 4);

-- SELECT * FROM joins;
-- INSERT INTO joins VALUES("3149607356", 5);
-- SELECT phone FROM joins WHERE gid = 1;
-- SELECT attendees FROM games WHERE gid = 1;

-- SELECT * FROM games WHERE EXISTS (SELECT * FROM joins WHERE games.gid = joins.gid AND phone = "6366750378");
-- SELECT gid FROM joins WHERE phone = "6366750378";
-- UPDATE games SET attendees = 2 WHERE gid = 1;

-- SELECT * FROM courts;
-- INSERT INTO courts (address, public, outside, openTime, closeTime)
-- Values("604, Constitution Ave, Fort Leonard Wood, MO 65473", 1, 1, "13:00", "19:00");

-- SELECT * FROM holds;
-- INSERT INTO holds VALUES (2, 5);

-- SELECT * FROM pools;
-- INSERT INTO pools VALUES (13, 2);

-- SELECT * FROM tennisCourts;
-- INSERT INTO tennisCourts VALUES (2, 10);

-- SELECT * FROM basketballCourts;
-- INSERT INTO basketballCourts VALUES (6, 2);

-- SELECT * FROM soccerFields;
-- INSERT INTO soccerFields VALUES (10, 1);

-- SELECT courts.cid, courts.address, courts.public, courts.outside, courts.openTime, courts.closeTime, tennisCourts.numNets
-- FROM courts, tennisCourts WHERE courts.cid = tennisCourts.cid;
-- 
-- SELECT games.gid, games.sport, games.description, games.gameDate, games.startTime, games.endTime, games.attendees, games.filled, courts.address
-- FROM games, courts WHERE EXISTS (SELECT * FROM holds WHERE games.gid = holds.gid AND holds.cid = courts.cid AND gid = 2)

-- SELECT * FROM games WHERE gid NOT IN (SELECT gid FROM joins WHERE joins.phone = "6366750378");