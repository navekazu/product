-- -----------------------------------------------------
-- member
insert into `member`(`name`, `login_id`, `password`, `mail_address`, `kind`, `status`) values('Administrator', 'admin', '$2a$10$hBYDoJUSEgpBSHAxaEMlje2S0SEiSXyDkQ8a8VYc6LVxUfPo8rqTa', 'foobar', 0, 1);
insert into `member`(`name`, `login_id`, `password`, `mail_address`, `kind`, `status`) values('test user 01', 'navekazu', '$2a$10$hBYDoJUSEgpBSHAxaEMlje2S0SEiSXyDkQ8a8VYc6LVxUfPo8rqTa', 'foobar', 1, 1);

-- question_master
insert into `question_master`(`question_master_id`, `name`, `choice_count`) values(1, 'AWS-基礎', 4);
insert into `question`(`question_id`, `question_master_id`, `contents`) values(1, 1, `AWSは、何の略？`);
insert into `question_choice`(`question_id`, `label`, `correct_flag`) values(1, 'あんなに わくわく するの？', false);
insert into `question_choice`(`question_id`, `label`, `correct_flag`) values(2, 'あんなに わくわく するの？', false);
insert into `question_choice`(`question_id`, `label`, `correct_flag`) values(3, 'あんなに わくわく するの？', false);
insert into `question_choice`(`question_id`, `label`, `correct_flag`) values(4, 'Amazon Web Service', true);
