-- Clinica Medica - estrutura atualizada para MySQL 8.0 ou superior
-- Gerado em 26/10/2026 a partir da estrutura utilizada pela aplicacao.
-- Script unico: inclui a estrutura da clinica e a autenticacao de usuarios.
-- Contem somente a estrutura. Nenhum usuario, senha ou dado clinico e exportado.
-- Nao apaga o banco nem os dados existentes. Tabelas existentes nao sao migradas.
-- Para uma instalacao nova, execute este arquivo completo no MySQL ou HeidiSQL.

SET NAMES utf8mb4;
CREATE DATABASE IF NOT EXISTS `clinica_medica` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `clinica_medica`;

-- Tabela: convenio
CREATE TABLE IF NOT EXISTS `convenio` (
  `id_convenio` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `registro_ans` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `telefone_contato` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id_convenio`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabela: consultorio
CREATE TABLE IF NOT EXISTS `consultorio` (
  `id_consultorio` int NOT NULL AUTO_INCREMENT,
  `numero` int NOT NULL DEFAULT '0',
  `andar` int NOT NULL DEFAULT '0',
  `capacidade` int DEFAULT NULL,
  PRIMARY KEY (`id_consultorio`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabela: especialidade
CREATE TABLE IF NOT EXISTS `especialidade` (
  `id_especialidade` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `descricao` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id_especialidade`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabela: funcionario
CREATE TABLE IF NOT EXISTS `funcionario` (
  `id_funcionario` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '0',
  `telefone` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '0',
  `email` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '0',
  `endereco` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '0',
  `salario` double NOT NULL DEFAULT (0),
  `cargo` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '0',
  PRIMARY KEY (`id_funcionario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabela: remedio
CREATE TABLE IF NOT EXISTS `remedio` (
  `id_remedio` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id_remedio`) USING BTREE,
  UNIQUE KEY `uk_remedio_nome` (`nome`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabela: usuario
-- Inclui o cadastro, login e recuperacao escolar por e-mail, sem SMTP ou tokens.
CREATE TABLE IF NOT EXISTS `usuario` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nomeusuario` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `senhahash` varchar(60) COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `nomeusuario` (`nomeusuario`),
  UNIQUE KEY `uk_usuario_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabela: medico
CREATE TABLE IF NOT EXISTS `medico` (
  `id_medico` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `telefone` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `crm` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `especialidade_id` int NOT NULL,
  `email` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `endereco` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id_medico`),
  KEY `FK_medico_especialidade` (`especialidade_id`),
  CONSTRAINT `fk_medico_especialidade` FOREIGN KEY (`especialidade_id`) REFERENCES `especialidade` (`id_especialidade`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabela: paciente
CREATE TABLE IF NOT EXISTS `paciente` (
  `id_paciente` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `data_nascimento` date NOT NULL,
  `cpf` varchar(14) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `convenio_id` int DEFAULT NULL,
  `telefone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `email` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `endereco` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id_paciente`) USING BTREE,
  KEY `fk_paciente_convenio` (`convenio_id`),
  CONSTRAINT `fk_paciente_convenio` FOREIGN KEY (`convenio_id`) REFERENCES `convenio` (`id_convenio`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabela: consulta
CREATE TABLE IF NOT EXISTS `consulta` (
  `id_consulta` int NOT NULL AUTO_INCREMENT,
  `data` date NOT NULL,
  `hora` time NOT NULL,
  `status` enum('agendada','realizada','cancelada') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'agendada',
  `medico_id` int NOT NULL,
  `paciente_id` int NOT NULL,
  `consultorio_id` int NOT NULL,
  PRIMARY KEY (`id_consulta`) USING BTREE,
  KEY `fk_consulta_medico` (`medico_id`),
  KEY `fk_consulta_paciente` (`paciente_id`),
  KEY `consultorio_id` (`consultorio_id`),
  CONSTRAINT `fk_consulta_consultorio` FOREIGN KEY (`consultorio_id`) REFERENCES `consultorio` (`id_consultorio`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_consulta_medico` FOREIGN KEY (`medico_id`) REFERENCES `medico` (`id_medico`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_consulta_paciente` FOREIGN KEY (`paciente_id`) REFERENCES `paciente` (`id_paciente`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabela: receita
CREATE TABLE IF NOT EXISTS `receita` (
  `id_receita` int NOT NULL AUTO_INCREMENT,
  `data_emissao` date NOT NULL,
  `observacoes` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `consulta_id` int NOT NULL,
  PRIMARY KEY (`id_receita`) USING BTREE,
  KEY `fk_receita_consulta` (`consulta_id`),
  CONSTRAINT `fk_receita_consulta` FOREIGN KEY (`consulta_id`) REFERENCES `consulta` (`id_consulta`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabela: item_receita
CREATE TABLE IF NOT EXISTS `item_receita` (
  `id_itemReceita` int NOT NULL AUTO_INCREMENT,
  `medicamento` int NOT NULL,
  `dosagem` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `posologia` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `receita_id` int NOT NULL,
  PRIMARY KEY (`id_itemReceita`) USING BTREE,
  KEY `fk_item_receita_receita` (`receita_id`),
  KEY `fk_item_receita_remedio` (`medicamento`),
  CONSTRAINT `fk_item_receita_receita` FOREIGN KEY (`receita_id`) REFERENCES `receita` (`id_receita`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_item_receita_remedio` FOREIGN KEY (`medicamento`) REFERENCES `remedio` (`id_remedio`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabela: prontuario
CREATE TABLE IF NOT EXISTS `prontuario` (
  `id_prontuario` int NOT NULL AUTO_INCREMENT,
  `diagnostico` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `descricao_sintomas` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '',
  `data_registro` date NOT NULL,
  `consulta_id` int NOT NULL,
  PRIMARY KEY (`id_prontuario`) USING BTREE,
  KEY `fk_prontuario_consulta` (`consulta_id`),
  CONSTRAINT `fk_prontuario_consulta` FOREIGN KEY (`consulta_id`) REFERENCES `consulta` (`id_consulta`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabela: tipo_exame
CREATE TABLE IF NOT EXISTS `tipo_exame` (
  `id_tipoExame` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `descricao` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `valor` double NOT NULL,
  `funcionario_id` int NOT NULL,
  `consultorio_id` int DEFAULT NULL,
  PRIMARY KEY (`id_tipoExame`) USING BTREE,
  KEY `funcionario_id` (`funcionario_id`),
  KEY `consultorio_id` (`consultorio_id`),
  CONSTRAINT `fk_tipo_exame_consultorio` FOREIGN KEY (`consultorio_id`) REFERENCES `consultorio` (`id_consultorio`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_tipo_exame_funcionario` FOREIGN KEY (`funcionario_id`) REFERENCES `funcionario` (`id_funcionario`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabela: solicitacao_exame
CREATE TABLE IF NOT EXISTS `solicitacao_exame` (
  `id_solicitacaoExame` int NOT NULL AUTO_INCREMENT,
  `data` date NOT NULL,
  `resultado` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `consulta_id` int NOT NULL,
  `tipo_exame_id` int NOT NULL,
  `funcionario_id` int NOT NULL,
  PRIMARY KEY (`id_solicitacaoExame`) USING BTREE,
  KEY `fk_solicitacao_consulta` (`consulta_id`),
  KEY `fk_solicitacao_tipo_exame` (`tipo_exame_id`),
  KEY `funcionario_id` (`funcionario_id`),
  CONSTRAINT `fk_solicitacao_consulta` FOREIGN KEY (`consulta_id`) REFERENCES `consulta` (`id_consulta`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_solicitacao_exame_funcionario` FOREIGN KEY (`funcionario_id`) REFERENCES `funcionario` (`id_funcionario`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_solicitacao_tipo_exame` FOREIGN KEY (`tipo_exame_id`) REFERENCES `tipo_exame` (`id_tipoExame`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Consulta auxiliar para listar os medicamentos de cada receita.
-- Sem DEFINER para funcionar com o usuario MySQL da maquina de destino.
CREATE OR REPLACE SQL SECURITY INVOKER VIEW `vw_itens_receita_com_remedio` AS
SELECT ir.`id_itemReceita`, ir.`receita_id`, r.`id_remedio` AS `medicamento_id`,
       r.`nome` AS `medicamento`, ir.`dosagem`, ir.`posologia`
FROM `item_receita` ir
INNER JOIN `remedio` r ON r.`id_remedio` = ir.`medicamento`;

-- A recuperacao escolar usa diretamente a tabela usuario. Nao exige tokens nem SMTP.
