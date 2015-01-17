--
-- Datenbank: `puzzle2`
--

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `answer`
--

CREATE TABLE IF NOT EXISTS `answer` (
`id` int(11) NOT NULL,
  `text` text NOT NULL,
  `is_correct` tinyint(1) NOT NULL,
  `questionREF` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `puzzle`
--

CREATE TABLE IF NOT EXISTS `puzzle` (
`id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `puzzle_part`
--

CREATE TABLE IF NOT EXISTS `puzzle_part` (
`id` int(11) NOT NULL,
  `uuid` varchar(36) NOT NULL,
  `order` smallint(6) NOT NULL,
  `image` mediumblob NOT NULL,
  `puzzleREF` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `question`
--

CREATE TABLE IF NOT EXISTS `question` (
`id` int(11) NOT NULL,
  `text` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Indizes der exportierten Tabellen
--

--
-- Indizes für die Tabelle `answer`
--
ALTER TABLE `answer`
 ADD PRIMARY KEY (`id`), ADD KEY `questionREF` (`questionREF`);

--
-- Indizes für die Tabelle `puzzle`
--
ALTER TABLE `puzzle`
 ADD PRIMARY KEY (`id`);

--
-- Indizes für die Tabelle `puzzle_part`
--
ALTER TABLE `puzzle_part`
 ADD PRIMARY KEY (`id`), ADD UNIQUE KEY `uuid` (`uuid`), ADD KEY `puzzleREF` (`puzzleREF`);

--
-- Indizes für die Tabelle `question`
--
ALTER TABLE `question`
 ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT für exportierte Tabellen
--

--
-- AUTO_INCREMENT für Tabelle `answer`
--
ALTER TABLE `answer`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT für Tabelle `puzzle`
--
ALTER TABLE `puzzle`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT für Tabelle `puzzle_part`
--
ALTER TABLE `puzzle_part`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT für Tabelle `question`
--
ALTER TABLE `question`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- Constraints der exportierten Tabellen
--

--
-- Constraints der Tabelle `answer`
--
ALTER TABLE `answer`
ADD CONSTRAINT `answer_ibfk_1` FOREIGN KEY (`questionREF`) REFERENCES `question` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints der Tabelle `puzzle_part`
--
ALTER TABLE `puzzle_part`
ADD CONSTRAINT `puzzle_part_ibfk_1` FOREIGN KEY (`puzzleREF`) REFERENCES `puzzle` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
