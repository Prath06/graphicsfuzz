/*
 * Copyright 2018 The GraphicsFuzz Project Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.graphicsfuzz.reducer.filejudge;

import com.graphicsfuzz.reducer.FileJudgeException;
import com.graphicsfuzz.reducer.IFileJudge;
import com.graphicsfuzz.util.ExecHelper;
import java.io.File;
import java.io.IOException;

public class CustomFileJudge implements IFileJudge {

  private final File judgeScript;
  private final File directory;

  public CustomFileJudge(File judgeScript, File directory) {
    this.judgeScript = judgeScript;
    this.directory = directory;
  }

  @Override
  public boolean isInteresting(File shaderJobFile, File shaderResultFileOutput)
      throws FileJudgeException {
    try {
      return new ExecHelper().exec(
          ExecHelper.RedirectType.TO_BUFFER,
          directory,
          true,
          judgeScript.getAbsolutePath(),
          shaderJobFile.getAbsolutePath(),
          shaderResultFileOutput.getAbsolutePath()).res == 0;
    } catch (IOException | InterruptedException exception) {
      throw new FileJudgeException(exception);
    }
  }
}
