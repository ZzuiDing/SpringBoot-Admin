import subprocess
import datetime
from collections import defaultdict
import csv

def get_git_log(days=7):
    since = f"{days} days ago"
    cmd = ['git', 'log', f'--since={since}', '--pretty=tformat:%ad', '--date=short', '--numstat']
    result = subprocess.run(cmd, stdout=subprocess.PIPE, stderr=subprocess.PIPE, text=True)

    if result.returncode != 0:
        print("❌ Git命令执行失败：", result.stderr)
        return None

    return result.stdout

def analyze_log(log_data):
    daily_stats = defaultdict(lambda: {'add': 0, 'del': 0})

    current_date = ""
    for line in log_data.splitlines():
        if line.strip() == "":
            continue
        parts = line.split()
        if len(parts) == 1:
            current_date = parts[0]
        elif len(parts) >= 3:
            try:
                added = int(parts[0]) if parts[0] != '-' else 0
                deleted = int(parts[1]) if parts[1] != '-' else 0
                daily_stats[current_date]['add'] += added
                daily_stats[current_date]['del'] += deleted
            except ValueError:
                continue

    return daily_stats

def output_csv(stats, filename="git_daily_stats.csv"):
    with open(filename, 'w', newline='', encoding='utf-8') as csvfile:
        writer = csv.writer(csvfile)
        writer.writerow(['日期', '新增行', '删除行', '总变动'])
        for date in sorted(stats):
            add = stats[date]['add']
            delete = stats[date]['del']
            writer.writerow([date, add, delete, add + delete])
    print(f"✅ 已保存到文件：{filename}")

if __name__ == "__main__":
    days = 50  # 可修改时间范围
    log = get_git_log(days)
    if log:
        stats = analyze_log(log)
        for date in sorted(stats):
            add = stats[date]['add']
            delete = stats[date]['del']
            print(f"{date}: ➕新增 {add} 行, ➖删除 {delete} 行, ✏️总变动 {add + delete}")
        output_csv(stats)
