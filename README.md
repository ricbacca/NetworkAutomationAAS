**To compile:**
1. Compile Docker images
2. Open App (Java Project) and run "gradle build"
3. Verify in Kathara/aas_project/ there must be a jar called "OVS_AAS.jar"
4. If everything ok, go to Kathara folder and run: "kathara lstart"



**To compile docker images:**
1. Go to DockerImages folder
2. In each folder execute "sudo docker build -t dockerImageName ."

**For the name of docker images:**
- From folder "a_b_c"
- Docker image MUST be named: "a/b_c"
- So you must substitute the first underscore with a "/"
