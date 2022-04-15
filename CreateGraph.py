import sys
import matplotlib.pyplot as plt
import datetime

def convertDate(date : str):
    arr = list(map(int,date.split("-")))
    return datetime.datetime(arr[2],arr[1],arr[0])

print(sys.argv)

p_name = sys.argv[1]
s_date = convertDate(sys.argv[2])
e_date = convertDate(sys.argv[3])

x_ticks = []
data = []

for i in range(0,(e_date-s_date).days+1):
    x_ticks.append(s_date+datetime.timedelta(days=i))
    data.append(float(sys.argv[4+i]))

plt.figure()
plt.title(p_name+" Sales")
plt.xlabel("Date")
plt.ylabel("Quantity Sold")
plt.plot(x_ticks,data)
plt.xticks(rotation=90)
plt.savefig("Graph.jpg")
