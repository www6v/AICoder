import threading
import time

def worker(thread_name, delay):
    """工作线程函数"""
    print(f"{thread_name} 开始工作")
    for i in range(3):
        time.sleep(delay)
        print(f"{thread_name} 正在执行任务 {i + 1}")
    print(f"{thread_name} 完成工作")

def main():
    # 创建三个线程
    thread1 = threading.Thread(target=worker, args=("线程1", 1))
    thread2 = threading.Thread(target=worker, args=("线程2", 2))
    thread3 = threading.Thread(target=worker, args=("线程3", 1.5))  # 添加第三个线程

    # 启动线程
    thread1.start()
    thread2.start()
    thread3.start()  # 启动第三个线程

    # 等待三个线程都完成
    thread1.join()
    thread2.join()
    thread3.join()  # 等待第三个线程完成

    print("所有线程执行完毕！")

if __name__ == "__main__":
    main() 