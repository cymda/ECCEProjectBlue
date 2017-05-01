from flask import Flask, json, render_template, request, jsonify
app = Flask(__name__)

import initialdata
users = initialdata.initialusers
professors = initialdata.initialprofessors

@app.route("/getUserList", methods=['GET'])
def get_user():
    jsonStr = json.dumps(users)
    return jsonStr

@app.route("/getProfessorList", methods=['GET'])
def get_professor():
    professorsString= json.dumps(professors)
    return professorsString

@app.route("/getReviewList", methods=['GET'])
def get_review_list():
    professorname = request.args.get('professorname')
    coursetitle = request.args.get('coursetitle')
    prof_id = 0
    for i in range(0, len(professors['professors'])):
        if professorname == professors['professors'][i]['professorname']:
            prof_id = i
    course_id = 0
    for i in range(0, len(professors['professors'][prof_id]['courses'])):
        if coursetitle == professors['professors'][prof_id]['courses'][i]['coursetitle']:
            course_id = i

    if coursetitle == 'General':
        print(professors['professors'][prof_id]['generalreviews'])
        return json.dumps(professors['professors'][prof_id]['generalreviews'])
    else:
        print(professors['professors'][prof_id]['courses'][course_id]['reviews'])
        return json.dumps(professors['professors'][prof_id]['courses'][course_id]['reviews'])

@app.route("/postReview", methods=['GET', 'POST'])
def post_review():
    if request.method == 'POST':
        professorname = request.args.get('professorname')
        coursetitle = request.args.get('coursetitle')

        prof_id = 0
        for i in range(0, len(professors['professors'])):
            if professorname == professors['professors'][i]['professorname']:
                prof_id = i
        course_id = 0
        for j in range(0, len(professors['professors'][prof_id]['courses'])):
            if coursetitle == professors['professors'][prof_id]['courses'][j]['coursetitle']:
                course_id = j

        review = json.loads(request.form['new_review'])

        if coursetitle == 'General':
            professors['professors'][prof_id]['generalreviews'].append(review)
        else :
            professors['professors'][prof_id]['courses'][course_id]['reviews'].append(review)

        return jsonify(professors)

@app.route("/registerUser", methods=['GET', 'POST'])
def register_user():
    if request.method == 'POST':
        newuser= json.loads(request.form['new_user'])
        username = newuser['username']
        password = newuser['password']
        users['users'].append({'username':username,
                               'password':password})
        return jsonify(users)

##   Run the web app for the server.
if __name__ == "__main__":
    app.debug = True
    app.run(host='0.0.0.0', port=8000)