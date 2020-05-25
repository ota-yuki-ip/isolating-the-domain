package example.domain.model.timerecord.evaluation;

import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * 勤務実績一覧
 */
public class TimeRecords {
    List<TimeRecord> list;

    public TimeRecords(List<TimeRecord> list) {
        this.list = list;
    }

    public List<TimeRecord> list() {
        return list;
    }

    public TimeRecord at(WorkDate day) {
        return list.stream()
                .filter(worked -> worked.isWorkedAt(day))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(day.toString()));
    }

    public Recorded recordedAt(WorkDate workDate) {
        return list.stream().anyMatch(timeRecord -> timeRecord.isWorkedAt(workDate)) ? Recorded.記録あり : Recorded.記録なし;
    }

    public AttendDates attendDates() {
        return new AttendDates(list.stream().map(TimeRecord::workDate).collect(toList()));
    }

    public TimeRecords weeklyRecords(WorkDate workDate) {
        return new TimeRecords(list.stream().filter(record -> record.workDate().sameWeek(workDate)).collect(toList()));
    }

    public TimeRecords recordsToDate(WorkDate workDate) {
        return new TimeRecords(list.stream().filter(record -> record.workDate().isBefore(workDate) || record.workDate().hasSameValue(workDate)).collect(toList()));
    }

    public TimeRecords recordsDayBefore(WorkDate workDate) {
        return new TimeRecords(list.stream().filter(record -> record.workDate().isBefore(workDate)).collect(toList()));
    }
}
