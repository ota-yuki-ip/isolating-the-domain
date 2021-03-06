package example.domain.model.timerecord.evaluation;

import example.domain.model.legislation.DaysOffStatus;
import example.domain.model.legislation.WeeklyWorkingHoursLimit;
import example.domain.model.legislation.WeeklyWorkingHoursStatus;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * 週の勤務実績
 */
public class WeeklyTimeRecord {
    TimeRecords value;

    public WeeklyTimeRecord(TimeRecords value) {
        this.value = value;
    }

    public static WeeklyTimeRecord from(MonthlyTimeRecord monthlyRecords, BeforeMonthlyTimeRecord beforeMonthlyRecords, WorkDate workDate) {
        List<TimeRecord> list = Stream.concat(
                beforeMonthlyRecords.list().stream(),
                monthlyRecords.list().stream()).collect(Collectors.toList());
        return new WeeklyTimeRecord(new TimeRecords(list.stream().filter(record -> record.workDate().sameWeek(workDate.value)).collect(toList())));
    }

    public WeeklyWorkingHoursStatus weeklyWorkingHoursStatus(WorkDate workDate) {
        if (recordsToDate(workDate).value.withinDailyLimitWorkTimeTotal().moreThan(WeeklyWorkingHoursLimit.legal().toMinute())) {
            return WeeklyWorkingHoursStatus.法定時間内労働時間の累計が４０時間を超えている;
        } else {
            return WeeklyWorkingHoursStatus.法定時間内労働時間の累計が４０時間以内;
        }
    }

    WeeklyTimeRecord recordsToDate(WorkDate workDate) {
        return new WeeklyTimeRecord(value.recordsToDate(workDate));
    }

    public Optional<TimeRecord> lastDayOff() {
        return value.list.stream()
                .filter(record -> record.daysOffStatus == DaysOffStatus.休日)
                .max(Comparator.comparing(r -> r.workDate().toDate()));
    }

    public WorkTimes workTimes() {
        return value.workTimes();
    }
}
