ALTER TABLE `t_student`
    MODIFY COLUMN `room_id` int(11) UNSIGNED NULL DEFAULT NULL AFTER `student_id`;

ALTER TABLE `t_student`
    ADD CONSTRAINT `fk_student_room` FOREIGN KEY (`room_id`) REFERENCES `t_room` (`room_id`);

ALTER TABLE `t_teacher_room`
    MODIFY COLUMN `teacher_id` int(11) UNSIGNED NULL DEFAULT NULL AFTER `teacher_room_id`,
    MODIFY COLUMN `room_id` int(11) UNSIGNED NULL DEFAULT NULL AFTER `teacher_id`;

ALTER TABLE `t_teacher_room`
    ADD CONSTRAINT `fk_tr_t` FOREIGN KEY (`teacher_id`) REFERENCES `t_teacher` (`teacher_id`);

ALTER TABLE `t_teacher_room`
    ADD CONSTRAINT `fk_tr_r` FOREIGN KEY (`room_id`) REFERENCES `t_room` (`room_id`);

ALTER TABLE `t_teacher`
    MODIFY COLUMN `subject_id` int(11) UNSIGNED NULL DEFAULT NULL AFTER `teacher_name`;

ALTER TABLE `t_teacher`
    ADD CONSTRAINT `fk_teacher_subject` FOREIGN KEY (`subject_id`) REFERENCES `t_subject` (`subject_id`);

