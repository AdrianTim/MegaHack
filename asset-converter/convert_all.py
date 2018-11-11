import subprocess
import sys, string, os, glob

base_output_path = ".\google-ar-asset-converter\output\\"
base_input_path = ".\google-ar-asset-converter\input\\"

for dir in os.listdir(path=base_input_path):
    local_input_file = str(base_input_path + dir + "\\" + dir +".obj")
    local_output_directory = str(base_output_path + dir)
    print("Opening .obj file : %s" % local_input_file) 
    
    os.system(".\google-ar-asset-converter\sceneform_sdk\windows\converter.exe --outdir " + local_output_directory + " " + local_input_file)
    print("\n\n")
##os.system(".\google-ar-asset-converter\sceneform_sdk\windows\converter.exe --outdir .\google-ar-asset-converter\output\ .\google-ar-asset-converter\input\\andy.obj")