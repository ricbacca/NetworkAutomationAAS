# NetworkAutomationAAS
Project for LM-32 thesis. Network Automation in Industry 4.0, using Asset Adiministration Shells.

**To compile:**
1. Compile Docker images
2. Open App (Java Project) and run "gradle build"
3. Verify in folder "Kathara/aas_project/" there must be a jar called "OVS_AAS.jar"
4. If everything ok, go to Kathara folder and run: "kathara lstart"



**To compile docker images:**
1. Go to DockerImages folder
2. In each folder execute "sudo docker build -t dockerImageName ."
3. Names must be:
  - kathara/basyx_registry
  - kathara/basyx_server
  - kathara/jdk
  - kathara/ryu
