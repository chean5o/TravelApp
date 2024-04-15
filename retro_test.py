from flask import Flask, jsonify

app = Flask(__name__)

@app.route('/posts', methods=['GET'])
def get_posts():
    # 예시 데이터
    posts = [
        {'id': 1, 'title': 'Hello World'},
        {'id': 2, 'title': 'Flask with Retrofit'}
    ]
    return jsonify(posts)

if __name__ == '__main__':
    app.run(debug=True)
