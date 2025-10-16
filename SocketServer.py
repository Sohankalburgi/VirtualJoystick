import socket
import threading
import time
from time import sleep

from pynput.keyboard import Controller, Key

HOST = '0.0.0.0'
PORT = 5051
keyboard = Controller()

# Key mapping
key_map = {
    "LEFT": Key.left,
    "RIGHT": Key.right,
    "UP": Key.up,
    "DOWN": Key.down,
    "TRIANGLE": 't',
    "CIRCLE": 'c',
    "SQUARE": 's',
}

key_pressed = {}
thread_stored = {}

def threadHandler(key):
    while key_pressed[key]:
        keyboard.press(key)
        sleep(0.05)
        keyboard.release(key)
        sleep(0.05)

    


print(f"[+] Starting server on {HOST}:{PORT}")
with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
    s.bind((HOST, PORT))
    s.listen(1)
    print("[+] Waiting for connection...")
    conn, addr = s.accept()
    with conn:
        print(f"[+] Connected by {addr}")
        while True:
            data = conn.recv(1024)
            if not data:
                break
            message = data.decode().split('_')
            action = message[0]
            isHeld = int(message[1])
            if action in key_map:
                key = key_map[action]
                print(key)
                if isHeld == 1:
                    key_pressed[key] = 1
                    thread = threading.Thread(target=threadHandler, args=(key,))
                    thread.start()
                    thread_stored[key] = thread
                elif isHeld == 0:
                    print("is Held is 0")
                    thread_stored[key].join()




                

